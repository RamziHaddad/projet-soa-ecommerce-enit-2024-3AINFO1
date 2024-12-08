package com.tn.soa.cart.service;

import com.tn.soa.cart.dto.CartDTO;
import com.tn.soa.cart.dto.CartItemDTO;
import com.tn.soa.cart.mapper.CartItemMapper;
import com.tn.soa.cart.model.Cart;
import com.tn.soa.cart.model.CartItem;
import com.tn.soa.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;

    @Value("${mock.server.url:http://mock-server:8082}")
    private String mockServerUrl;

    public CartService(CartRepository cartRepository, RestTemplate restTemplate) {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
    }

    public Cart getCart(UUID customerId) {
        Optional<Cart> cartOptional = cartRepository.findById(customerId);
        return cartOptional.orElse(new Cart()); // Return a new empty cart if not found
    }

    public Cart addItemToCart(UUID customerId, CartItemDTO itemDTO) {
        Optional<Cart> cartOptional = cartRepository.findById(customerId);
        Cart cart = cartOptional.orElse(new Cart()); // Create a new cart if none exists

        CartItem newItem = CartItemMapper.toModel(itemDTO);
        enrichItemDetails(newItem);

        cart.addItem(newItem);
        cartRepository.save(cart); // Save updated cart to database
        return cart;
    }

    private void enrichItemDetails(CartItem item) {
        try {
            String catalogUrl = mockServerUrl + "/catalog/item/" + item.getItemId();
            CartItemDTO response = restTemplate.getForObject(catalogUrl, CartItemDTO.class);

            if (response != null) {
                item.setName(response.getName());
                item.setPrice(response.getPrice());
                item.calculateTotalPrice();
            }
        } catch (Exception e) {
            item.setName("Unknown Item");
            item.setPrice(0.0);
            item.calculateTotalPrice();
        }
    }

    public Cart removeItemFromCart(UUID customerId, UUID itemId) {
        Optional<Cart> cartOptional = cartRepository.findById(customerId);
        Cart cart = cartOptional.orElse(new Cart());

        cart.getCartItems().removeIf(item -> item.getItemId().equals(itemId));
        cartRepository.save(cart); // Save updated cart to database
        return cart;
    }

    public void clearCart(UUID customerId) {
        cartRepository.deleteById(customerId); // Delete the cart from the database
    }

    public Cart createCart(UUID customerId) {
        Cart newCart = new Cart();
        cartRepository.save(newCart); // Save the new cart to the database
        return newCart;
    }

    public Cart createCart(CartDTO cartDTO) {
        Cart newCart = new Cart();
        if (cartDTO.getCartItems() != null) {
            for (CartItemDTO itemDTO : cartDTO.getCartItems()) {
                CartItem newItem = CartItemMapper.toModel(itemDTO);
                enrichItemDetails(newItem);
                newCart.addItem(newItem);
            }
        }
        UUID newCustomerId = UUID.randomUUID();  // Generate a new UUID for the new customer
        cartRepository.save(newCart);  // Save with the new UUID to the database
        return newCart;
    }
}
