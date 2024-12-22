package com.tn.soa.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import com.tn.soa.cart.dto.CartDTO;
import com.tn.soa.cart.dto.CartItemDTO;
import com.tn.soa.cart.mapper.CartItemMapper;
import com.tn.soa.cart.model.Cart;
import com.tn.soa.cart.model.CartItem;
import com.tn.soa.cart.repository.CartRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final RestTemplate restTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate; // Kafka producer
    private final ObjectMapper objectMapper; // Used for converting object to JSON

    @Value("${mock.server.url:http://mock-server:8082}")
    private String mockServerUrl;

    // Kafka Topics
    private static final String CART_UPDATE_TOPIC = "cart-update-topic";

    public CartService(CartRepository cartRepository, RestTemplate restTemplate,
            KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.cartRepository = cartRepository;
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Cart getCart(UUID customerId) {
        Optional<Cart> cartOptional = cartRepository.findById(customerId);
        return cartOptional.orElse(new Cart());
    }

    public Cart addItemToCart(UUID customerId, CartItemDTO itemDTO) {
        Optional<Cart> cartOptional = cartRepository.findById(customerId);
        Cart cart = cartOptional.orElse(new Cart());

        CartItem newItem = CartItemMapper.toModel(itemDTO);
        enrichItemDetails(newItem);

        cart.addItem(newItem);
        cartRepository.save(cart);

        // Send Kafka message after adding item to cart
        sendCartUpdateMessage(cart);

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
        cartRepository.save(cart);

        // Send Kafka message after removing item from cart
        sendCartUpdateMessage(cart);

        return cart;
    }

    public void clearCart(UUID customerId) {
        cartRepository.deleteById(customerId);
    }

    public Cart createCart(UUID customerId) {
        Cart newCart = new Cart();
        cartRepository.save(newCart);
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
        UUID newCustomerId = UUID.randomUUID();
        cartRepository.save(newCart);

        // Send Kafka message after creating new cart
        sendCartUpdateMessage(newCart);

        return newCart;
    }

    // Method to send Kafka message after any update in cart
    private void sendCartUpdateMessage(Cart cart) {
        try {
            String cartJson = objectMapper.writeValueAsString(cart);
            kafkaTemplate.send(CART_UPDATE_TOPIC, cart.getCustomerId().toString(), cartJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Log or handle exception accordingly
        }
    }

    // Kafka listener to handle updates (you can add this based on your use case)
    @KafkaListener(topics = CART_UPDATE_TOPIC, groupId = "cart-group")
    public void handleCartUpdateResult(String message, Acknowledgment acknowledgment) {
        try {
            // Process cart update result if needed
            System.out.println("Received cart update: " + message);

            // Acknowledge the message
            acknowledgment.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
