package com.example.Catalog.DTO.ProductDTO;

import com.example.Catalog.Entities.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private ProductCategory category;

}
