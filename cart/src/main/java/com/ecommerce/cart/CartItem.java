package com.ecommerce.cart;

public class CartItem {
    private int productId;
    private int quantity;

    // Constructor
    public CartItem(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
