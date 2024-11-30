package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.cart.repository.CartRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // Change String to Long for userId
    public java.util.List<CartItem> getCart(Long userId) {
        return cartRepository.findByUserId(userId);  // Return it directly as a java.util.List
    }

    public CartItem addItemToCart(CartDTO cartDTO) {
        CartItem item = new CartItem();
        item.setUserId(cartDTO.getUserId());
        item.setProductId(cartDTO.getProductId());
        item.setQuantity(cartDTO.getQuantity());
        cartRepository.addCartItem(item);
        return item;
    }

    public void removeItem(Long itemId) {
        cartRepository.removeCartItem(itemId);
    }
}
