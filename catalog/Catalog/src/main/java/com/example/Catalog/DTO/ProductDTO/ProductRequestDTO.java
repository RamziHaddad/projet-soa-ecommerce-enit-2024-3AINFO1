package com.example.Catalog.DTO.ProductDTO;

import com.example.Catalog.Entities.ProductCategory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {


        private String name;
        private String description;
        private double price;
        private ProductCategory category;




}
