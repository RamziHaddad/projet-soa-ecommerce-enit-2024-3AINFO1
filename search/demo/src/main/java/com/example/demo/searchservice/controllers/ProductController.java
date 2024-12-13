package com.example.demo.searchservice.controllers;

import com.example.demo.searchservice.services.ProductService;
import com.example.demo.searchservice.models.Product;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Méthode pour ajouter un produit
    @PostMapping("/index")
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
            List<Product> products = productService.getAllProductsFromSolr("products-index");
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Product>> getSimilarProducts(@PathVariable("id") String id) {
        List<Product> similarProducts = productService.findSimilarProducts("products-index", id);

        if (similarProducts != null && !similarProducts.isEmpty()) {
            // Log de la réponse obtenue
            System.out.println("Returning similar products: " + similarProducts);
            return ResponseEntity.ok(similarProducts); // Retourne les produits en réponse
        } else {
            System.out.println("No similar products found.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Si aucun produit similaire trouvé
        }
    }

    @GetMapping("/search/text")
    public ResponseEntity<List<Product>> searchByText(
            @RequestParam String queryText) {
        try {
            List<Product> products = productService.searchByText("products-index", queryText);
            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
