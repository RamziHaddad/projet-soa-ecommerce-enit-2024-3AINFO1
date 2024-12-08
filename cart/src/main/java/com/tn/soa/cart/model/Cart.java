package com.tn.soa.cart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID customerId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Method to calculate the total price of the cart
    public double calculateTotal() {
        return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    // Method to add an item to the cart
    public void addItem(CartItem item) {
        cartItems.add(item);
        item.setCart(this);  // Set the cart reference in the item
    }

    // Method to remove an item from the cart
    public void removeItem(CartItem item) {
        cartItems.remove(item);
    }
}
