package com.example.demo.searchservice.models;

public class Product {

    private String id;
    private String description;

    // Constructeur
    public Product(String id, String description) {
        this.id = id;
        this.description = description;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
