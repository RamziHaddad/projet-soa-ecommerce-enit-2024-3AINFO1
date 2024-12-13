package com.example.demo.searchservice.models;

import org.apache.solr.client.solrj.beans.Field;

import java.util.List;

public class Product {

    @Field
    private String id;

    @Field
    private List<String> description; // Champ description en tant que liste

    // Constructeurs
    public Product() {
    }

    public Product(String id, List<String> description) {
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

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", description=" + description +
                '}';
    }
}
