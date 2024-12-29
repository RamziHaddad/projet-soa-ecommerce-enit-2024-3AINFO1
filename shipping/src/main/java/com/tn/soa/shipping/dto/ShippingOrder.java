package com.tn.soa.shipping.dto;

import java.util.List;
import java.util.UUID;

public class ShippingOrder {
    private UUID orderId;
    private UUID customerId;
    private List<OrderItem> products;

    // Getters and setters
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }
}
