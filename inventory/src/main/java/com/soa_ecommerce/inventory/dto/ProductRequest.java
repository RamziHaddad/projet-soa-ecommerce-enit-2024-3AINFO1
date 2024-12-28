package com.soa_ecommerce.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import java.util.UUID;

public record ProductRequest(
        @NotNull(message = "Product Id must be not null")
        UUID productId,

        @Positive(message = "quantity must be positive")
        @Min(value = 1,message = "Minimum 1")
        Integer quantity
) {
}
