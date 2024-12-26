package tn.soa_ecommerce.order.service;

import org.springframework.stereotype.Service;
import tn.soa_ecommerce.order.dto.OrderDTO;
import tn.soa_ecommerce.order.dto.OrderItemDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.Order;
import tn.soa_ecommerce.order.model.OrderItem;
import tn.soa_ecommerce.order.model.OrderStatus;
import tn.soa_ecommerce.order.repository.OrderRepository;

import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final Mapper<Order, OrderDTO> orderMapper;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final MailingService mailingService;

    public OrderService(
            OrderRepository orderRepository,
            Mapper<Order, OrderDTO> orderMapper,
            InventoryService inventoryService,
            PaymentService paymentService,
            ShippingService shippingService,
            MailingService mailingService) {

        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.shippingService = shippingService;
        this.mailingService = mailingService;
    }

    public boolean createOrder(Order order_) {

        try {
            order_.setStatus(OrderStatus.CREATED);
            Order order = orderRepository.save(order_);

         // Step 1: Reserve products in the inventory
            boolean isReserved = inventoryService.reserveProducts(order.getOrderID(), order.getItems());
            if (!isReserved) {
                order.setStatus(OrderStatus.FAILED);
                return false;
            }
            order.setStatus(OrderStatus.RESERVED);

         // Step 2: Process payment
            boolean isPaymentSuccessful = paymentService.processPayment(order.getOrderID(), order.getCustomerID(), order.getTotalAmount());
            if (!isPaymentSuccessful) {
                order.setStatus(OrderStatus.FAILED);
                inventoryService.cancelReservation(order.getOrderID());
                return false;
            }
            order.setStatus(OrderStatus.PAID);
            inventoryService.releaseReservation(order.getOrderID());

         // Step 3: Schedule shipping
            boolean isShippingScheduled = shippingService.scheduleShipping(order.getOrderID(), order.getCustomerID(),order.getItems());
            if (!isShippingScheduled) {
                order.setStatus(OrderStatus.FAILED);
                inventoryService.cancelReservation(order.getOrderID());
                paymentService.refundPayment(order.getOrderID());
                return false;
            }
            order.setStatus(OrderStatus.SHIPPING_SCHEDULED);

         // Step 4: Send email notification
            boolean isEmailSent = mailingService.sendEmail(order.getOrderID(), order.getCustomerID(), order.getStatus(), order.getTotalAmount());
            if (!isEmailSent) {
                order.setStatus(OrderStatus.FAILED);
                inventoryService.cancelReservation(order.getOrderID());
                paymentService.refundPayment(order.getOrderID());
                return false;
            }

            order.setStatus(OrderStatus.COMPLETED);
            return true;

        } catch (RuntimeException e) {
            order_.setStatus(OrderStatus.FAILED);
            orderRepository.save(order_);
            throw e;
        }
    }

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public boolean cancelOrder(UUID id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            switch (order.getStatus()) {
                case CREATED:
                    order.setStatus(OrderStatus.CANCELED);
                    orderRepository.save(order);
                    return true;

                case RESERVED:
                    boolean isInventoryCancelled = inventoryService.cancelReservation(order.getOrderID());
                    if (isInventoryCancelled) {
                        order.setStatus(OrderStatus.CANCELED);
                        orderRepository.save(order);
                        return true;
                    }
                    break;

                case PAID:
                    boolean isInventoryCancelledPaid = inventoryService.cancelReservation(order.getOrderID());
                    if ( isInventoryCancelledPaid) {
                        boolean isPaymentRefunded = paymentService.refundPayment(order.getOrderID());
                        if(isPaymentRefunded){
                            order.setStatus(OrderStatus.CANCELED);
                            orderRepository.save(order);
                            return true;
                        }
                    }
                    break;

                default:
                    // If the order is already in shipping or completed, it cannot be canceled
                    return false;
            }
        }
        return false;
    }
}