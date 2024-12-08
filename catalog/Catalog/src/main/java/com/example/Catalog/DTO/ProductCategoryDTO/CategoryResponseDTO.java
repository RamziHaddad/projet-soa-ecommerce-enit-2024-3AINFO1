package com.example.Catalog.DTO.ProductCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
    private UUID id;
    private String categoryName;
}
