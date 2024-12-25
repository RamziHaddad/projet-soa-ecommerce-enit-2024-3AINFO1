package tn.soa_ecommerce.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import tn.soa_ecommerce.order.dto.OrderDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.Order;
import tn.soa_ecommerce.order.model.OrderStatus;
import tn.soa_ecommerce.order.producer.KafkaMessageProducer;
import tn.soa_ecommerce.order.repository.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final Mapper<Order, OrderDTO> orderMapper;
    private final KafkaMessageProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.inventory.reserve.request}")
    private String inventoryReserveTopic;

    @Value("${kafka.topic.payment.process.request}")
    private String paymentProcessTopic;

    @Value("${kafka.topic.shipping.schedule.request}")
    private String shippingScheduleTopic;

    @Value("${kafka.topic.notification.send.request}")
    private String notificationSendTopic;



    public OrderService(
            OrderRepository orderRepository,
            Mapper<Order, OrderDTO> orderMapper,
            KafkaMessageProducer kafkaProducer,
            ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    public OrderDTO createOrder(Order order) {
        try {
            order.setStatus(OrderStatus.CREATED);
            Order savedOrder = orderRepository.save(order);
            logger.info("Created order: {}", savedOrder.getOrderID());

            Map<String, Object> inventoryDetails = new HashMap<>();
            inventoryDetails.put("orderId", savedOrder.getOrderID());
            inventoryDetails.put("items", order.getItems());

            kafkaProducer.sendMessage(
                    inventoryReserveTopic,
                    savedOrder.getOrderID().toString(),
                    inventoryDetails
            );

            return orderMapper.mapTo(savedOrder);

        } catch (Exception e) {
            logger.error("Error creating order", e);
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            throw e;
        }
    }

    @KafkaListener(topics = "${kafka.topic.inventory.reserve.result}", groupId = "order-group")
    public void handleInventoryReservationResult(String message, Acknowledgment acknowledgment) {
        processKafkaMessage(message, acknowledgment, "inventory reservation", (result, orderId) -> {
            if ((boolean) result.get("success")) {
                updateOrderStatus(orderId, OrderStatus.RESERVED);
                sendPaymentRequest(orderId);
            } else {
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }
        });
    }

    @KafkaListener(topics = "${kafka.topic.payment.process.result}", groupId = "order-group")
    public void handlePaymentProcessResult(String message, Acknowledgment acknowledgment) {
        processKafkaMessage(message, acknowledgment, "payment processing", (result, orderId) -> {
            if ((boolean) result.get("success")) {
                updateOrderStatus(orderId, OrderStatus.PAID);
                sendShippingRequest(orderId);
            } else {
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }
        });
    }

    @KafkaListener(topics = "${kafka.topic.shipping.schedule.result}", groupId = "order-group")
    public void handleShippingScheduleResult(String message, Acknowledgment acknowledgment) {
        processKafkaMessage(message, acknowledgment, "shipping scheduling", (result, orderId) -> {
            if ((boolean) result.get("success")) {
                updateOrderStatus(orderId, OrderStatus.SHIPPING_SCHEDULED);
                sendNotificationRequest(orderId);
            } else {
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }
        });
    }

    @KafkaListener(topics = "${kafka.topic.notification.send.result}", groupId = "order-group")
    public void handleNotificationSendResult(String message, Acknowledgment acknowledgment) {
        processKafkaMessage(message, acknowledgment, "notification sending", (result, orderId) -> {
            if ((boolean) result.get("success")) {
                updateOrderStatus(orderId, OrderStatus.COMPLETED);
                logger.info("Order {} completed successfully", orderId);
            } else {
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }
        });
    }

    private void processKafkaMessage(String message, Acknowledgment acknowledgment,
                                     String operation, ResultHandler handler) {
        try {
            Map<String, Object> result = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(result.get("orderId").toString());

            handler.handle(result, orderId);
            acknowledgment.acknowledge();

        } catch (Exception e) {
            logger.error("Error processing {} result", operation, e);
        }
    }

    private void sendPaymentRequest(UUID orderId) {
        getOrderById(orderId).ifPresent(order -> {
            try {
                Map<String, Object> paymentDetails = new HashMap<>();
                paymentDetails.put("orderId", order.getOrderID());
                paymentDetails.put("customerId", order.getCustomerID());
                paymentDetails.put("totalAmount", order.getTotalAmount());

                kafkaProducer.sendMessage(
                        paymentProcessTopic,
                        orderId.toString(),
                        paymentDetails
                );
                logger.info("Sent payment request for order: {}", orderId);
            } catch (Exception e) {
                logger.error("Error sending payment request for order: {}", orderId, e);
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }
        });
    }

    private void sendShippingRequest(UUID orderId) {
        getOrderById(orderId).ifPresent(order -> {
            try {
                Map<String, Object> shippingDetails = new HashMap<>();
                shippingDetails.put("orderId", order.getOrderID());
                shippingDetails.put("customerId", order.getCustomerID());

                kafkaProducer.sendMessage(
                        shippingScheduleTopic,
                        orderId.toString(),
                        shippingDetails
                );
                logger.info("Sent shipping request for order: {}", orderId);
            } catch (Exception e) {
                logger.error("Error sending shipping request for order: {}", orderId, e);
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }
        });
    }

    private void sendNotificationRequest(UUID orderId) {
        getOrderById(orderId).ifPresent(order -> {
            try {
                Map<String, Object> notificationDetails = new HashMap<>();
                notificationDetails.put("orderId", order.getOrderID());
                notificationDetails.put("customerId", order.getCustomerID());

                kafkaProducer.sendMessage(
                        notificationSendTopic,
                        orderId.toString(),
                        notificationDetails
                );
                logger.info("Sent notification request for order: {}", orderId);
            } catch (Exception e) {
                logger.error("Error sending notification request for order: {}", orderId, e);
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }
        });
    }

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public void updateOrderStatus(UUID orderId, OrderStatus status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
            logger.info("Updated order {} status to {}", orderId, status);
        });
    }

    @FunctionalInterface
    private interface ResultHandler {
        void handle(Map<String, Object> result, UUID orderId);
    }
}