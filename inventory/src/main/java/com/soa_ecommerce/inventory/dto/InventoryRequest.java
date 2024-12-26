package com.soa_ecommerce.inventory.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public record InventoryRequest(
        @NotNull(message = "Product Id must be not null")
        UUID orderId,
        @NotEmpty(message = "Product list cannot be empty")
        List<@Valid ProductRequest> products

) {
}
