package com.tn.soa.cart.mapper;

import com.tn.soa.cart.dto.CartItemDTO;
import com.tn.soa.cart.model.CartItem;

public class CartItemMapper {

    public static CartItemDTO toDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setItemId(cartItem.getItemId());
        dto.setName(cartItem.getName());
        dto.setPrice(cartItem.getPrice());
        dto.setQuantity(cartItem.getQuantity());
        dto.setTotalPrice(cartItem.getTotalPrice());
        return dto;
    }

    public static CartItem toModel(CartItemDTO dto) {
        // Initialize a new CartItem object
        CartItem cartItem = new CartItem();

        // Map basic properties from DTO to CartItem
        cartItem.setItemId(dto.getItemId());
        cartItem.setName(dto.getName());
        cartItem.setPrice(dto.getPrice());
        cartItem.setQuantity(dto.getQuantity());

        // Recalculate totalPrice based on price and quantity
        double totalPrice = dto.getPrice() * dto.getQuantity();
        cartItem.setTotalPrice(totalPrice);

        return cartItem;
    }
}
