package com.soa_ecommerce.inventory.api;


import com.soa_ecommerce.inventory.dto.InventoryRequest;
import com.soa_ecommerce.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> receiveProduct(
            @PathVariable UUID productId, @RequestParam Integer quantity
    ) {
        inventoryService.receiveProduct(productId, quantity);
        if (inventoryService.isExists(productId)) {
            return new ResponseEntity<>(HttpStatus.OK); // If product exists, return 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED); // If product doesn't exist, return 201 CREATED
        }
    }

    @PatchMapping("/release/{orderId}")
    public ResponseEntity<Void> releaseProduct(
             @PathVariable UUID orderId
    ) {
        inventoryService.releaseOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/reserve")
    public ResponseEntity<Void> reserveProducts(
            @RequestBody @Valid InventoryRequest request
    ) {
        inventoryService.reserveProduct(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<Void> cancelOrders(
            @PathVariable UUID orderId
    ) {
        inventoryService.cancelOrder(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}