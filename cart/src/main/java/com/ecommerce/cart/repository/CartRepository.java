package com.ecommerce.cart.repository;

import com.ecommerce.cart.entity.CartItem;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CartRepository implements PanacheRepositoryBase<CartItem, Long> {

    public java.util.List<CartItem> findByUserId(Long userId) {
        return find("userId", userId).list();
    }

    public void addCartItem(CartItem item) {
        persist(item); // Persist CartItem
    }

    public void removeCartItem(Long itemId) {
        CartItem item = findById(itemId);
        if (item != null) {
            delete(item); // Delete CartItem
        }
    }
}
