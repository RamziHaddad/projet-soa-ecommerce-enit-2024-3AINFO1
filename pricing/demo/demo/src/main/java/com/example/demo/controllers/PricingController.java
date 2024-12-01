package com.example.demo.controllers;

import com.example.demo.services.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    private final PricingService pricingService;

    // Constructor injection
    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Double> calculatePrice(@RequestBody Map<String, Object> requestBody) {
        try {
            // Extract productId and quantity from the request body
            Long productId = ((Number) requestBody.get("productId")).longValue();
            Integer quantity = ((Number) requestBody.get("quantity")).intValue();

            // Call the pricing service to calculate the price
            Double calculatedPrice = pricingService.calculatePrice(productId, quantity);
            if (calculatedPrice == null || calculatedPrice <= 0) {
                return ResponseEntity.badRequest().body(0.0); // Return 400 if price is invalid
            }
            return ResponseEntity.ok(calculatedPrice); // Return calculated price with 200 OK
        } catch (Exception e) {
            // Handle any exceptions, like missing data or calculation errors
            return ResponseEntity.status(500).body(0.0); // Return 500 if something goes wrong
        }
    }
}
