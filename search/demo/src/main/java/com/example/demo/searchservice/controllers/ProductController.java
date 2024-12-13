package com.example.demo.searchservice.controllers;

import com.example.demo.searchservice.services.ProductService;
import com.example.demo.searchservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/index")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Méthode pour ajouter un produit
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        try {

            productService.addProduct("products-index", product); // Indexer le produit via le service
            return ResponseEntity.status(HttpStatus.CREATED).body("Product indexed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error indexing product: " + e.getMessage());
        }
    }

    // Méthode pour récupérer tous les produits
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            System.out.println("Products retrieved: " + products);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




}
