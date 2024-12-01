package com.example.demo.services;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.Models.Promotion;
import com.example.demo.Repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PricingService {

    private final PromotionRepository promotionRepository;
    private final RestTemplate restTemplate;

    // Constructor injection
    public PricingService(PromotionRepository promotionRepository, RestTemplate restTemplate) {
        this.promotionRepository = promotionRepository;
        this.restTemplate = restTemplate;
    }

    public double calculatePrice(Long productId, Integer quantity) {
        double totalPrice = 0;

        // Retrieve product details via the catalog service
        String url = "http://localhost:8080/product/id/" + productId;  // Assuming catalog service is at this URL
        ProductDTO product = restTemplate.getForObject(url, ProductDTO.class);

        // Ensure the product exists
        if (product == null) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }

        double basePrice = product.getPrice();  // Use 'price' from ProductDTO as base price

        // Apply any applicable promotion
        Promotion promotion = promotionRepository.findByProductId(productId).orElse(null);
        if (promotion != null) {
            // Apply the promotion discount
            basePrice -= (basePrice * promotion.getDiscountPercentage() / 100);
        }

        // Calculate total price based on the quantity
        totalPrice = basePrice * quantity;

        return totalPrice;
    }
}
