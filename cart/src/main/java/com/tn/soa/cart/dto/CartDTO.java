package com.tn.soa.cart.dto;

import java.util.List;

public class CartDTO {

    private List<CartItemDTO> cartItems;
    private double totalPrice;

    // Getters and Setters
    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
