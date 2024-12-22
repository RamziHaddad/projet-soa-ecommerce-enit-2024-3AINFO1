package com.tn.soa.cart.controller;

import com.tn.soa.cart.dto.CartDTO;
import com.tn.soa.cart.dto.CartItemDTO;
import com.tn.soa.cart.mapper.CartMapper;
import com.tn.soa.cart.model.Cart;
import com.tn.soa.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable UUID customerId) {
        Cart cart = cartService.getCart(customerId);
        if (cart == null) {
            cart = new Cart();
        }
        return ResponseEntity.ok(CartMapper.toDTO(cart));
    }

    @PostMapping("/create")
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        try {
            Cart newCart = cartService.createCart(cartDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(CartMapper.toDTO(newCart));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/{customerId}/add")
    public ResponseEntity<CartDTO> addItemToCart(
            @PathVariable UUID customerId,
            @RequestBody CartItemDTO itemDTO) {
        try {
            Cart updatedCart = cartService.addItemToCart(customerId, itemDTO);
            return ResponseEntity.ok(CartMapper.toDTO(updatedCart));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{customerId}/remove/{itemId}")
    public ResponseEntity<CartDTO> removeItemFromCart(
            @PathVariable UUID customerId,
            @PathVariable UUID itemId) {
        try {
            Cart updatedCart = cartService.removeItemFromCart(customerId, itemId);
            return ResponseEntity.ok(CartMapper.toDTO(updatedCart));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{customerId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable UUID customerId) {
        try {
            cartService.clearCart(customerId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
