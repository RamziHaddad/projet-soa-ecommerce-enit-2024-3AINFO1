package com.tn.soa.cart.mapper;

import com.tn.soa.cart.dto.CartDTO;
import com.tn.soa.cart.dto.CartItemDTO;
import com.tn.soa.cart.model.Cart;

import java.util.stream.Collectors;

public class CartMapper {
    public static CartDTO toDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartItems(cart.getCartItems()
                .stream()
                .map(CartItemMapper::toDTO)
                .collect(Collectors.toList()));
        cartDTO.setTotalPrice(cart.calculateTotal());
        return cartDTO;
    }
}
