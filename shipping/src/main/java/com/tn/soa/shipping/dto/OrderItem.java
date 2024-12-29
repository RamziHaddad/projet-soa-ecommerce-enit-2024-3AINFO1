package com.tn.soa.shipping.dto;

import java.util.UUID;

public class OrderItem {
    private UUID productId;
    private int quantity;

    // Getters and setters
    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
