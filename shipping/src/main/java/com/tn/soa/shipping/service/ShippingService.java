package com.tn.soa.shipping.service;

import com.tn.soa.shipping.dto.ShippingOrder;
import com.tn.soa.shipping.model.CustomerAddress;
import com.tn.soa.shipping.model.ShippingStatus;
import com.tn.soa.shipping.repository.CustomerAddressRepository;
import com.tn.soa.shipping.repository.ShippingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ShippingService {

    @Autowired
    private ShippingOrderRepository shippingOrderRepository;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    public ShippingOrder scheduleShipping(UUID orderId, UUID customerId,
            List<com.tn.soa.shipping.dto.OrderItem> products) {
        // Validate customer address exists
        List<CustomerAddress> addresses = customerAddressRepository.findByCustomerId(customerId);
        if (addresses.isEmpty()) {
            throw new RuntimeException("No address found for customer");
        }

        // Create and save the shipping order
        com.tn.soa.shipping.model.ShippingOrder shippingOrder = new com.tn.soa.shipping.model.ShippingOrder();
        shippingOrder.setOrderId(orderId);
        shippingOrder.setCustomerId(customerId);
        shippingOrder.setStatus(ShippingStatus.PENDING);
        shippingOrder.setScheduledDate(LocalDateTime.now().plusDays(1)); // Example scheduling

        shippingOrderRepository.save(shippingOrder);

        // Return DTO
        ShippingOrder dto = new ShippingOrder();
        dto.setOrderId(orderId);
        dto.setCustomerId(customerId);
        dto.setProducts(products);
        return dto;
    }

    public boolean updateShippingStatus(UUID orderId, String status) {
        com.tn.soa.shipping.model.ShippingOrder shippingOrder = shippingOrderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Shipping order not found"));
        shippingOrder.setStatus(ShippingStatus.valueOf(status.toUpperCase()));
        shippingOrderRepository.save(shippingOrder);
        return true;
    }
}
