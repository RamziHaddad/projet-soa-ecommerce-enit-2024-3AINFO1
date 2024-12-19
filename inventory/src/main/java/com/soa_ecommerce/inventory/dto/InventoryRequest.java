package com.soa_ecommerce.inventory.dto;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record InventoryRequest(
        @NotNull(message = "Product Id must be not null")
        UUID orderId,
        @NotNull(message = "Product Id must be not null")
        UUID productId,
        @Positive(message = "quantity must be positive")
        @Min(value = 1,message = "Minimum 1")
        Integer quantity
) {
}
