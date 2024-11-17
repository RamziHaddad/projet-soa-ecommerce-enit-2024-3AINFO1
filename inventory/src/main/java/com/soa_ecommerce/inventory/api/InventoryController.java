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
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

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

    @PatchMapping("/release")
    public ResponseEntity<Void> releaseProduct(
             @RequestBody @Valid List<InventoryRequest> request
    ){
        inventoryService.releaseOrder(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/reserve")
    public ResponseEntity<Void> reserveProducts(
            @RequestBody @Valid List<InventoryRequest> requests
    ) {
        inventoryService.reserveProduct(requests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/cancel")
    public ResponseEntity<Void> cancelOrders(
            @RequestBody @Valid List<InventoryRequest> requests
    ) {
        inventoryService.cancelOrder(requests);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
