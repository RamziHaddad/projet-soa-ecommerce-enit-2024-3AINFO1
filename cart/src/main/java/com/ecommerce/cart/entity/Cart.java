package com.ecommerce.cart.entity;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public Long userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
@JoinColumn(name = "cart_id")
public List<CartItem> items = new ArrayList<>();  // Ensure items list is initialized


public Double totalPrice = 0.0; // Initialize to avoid null issues

}

