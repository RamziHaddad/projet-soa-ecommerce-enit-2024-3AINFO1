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
    /*

    private final InventoryService inventoryService;

    @PutMapping("/{productId}")
    public ResponseEntity receiveProduct(
            @PathVariable UUID productId, @RequestParam Integer quantity
    ) {
        inventoryService.receiveProduct(productId, quantity);
        if (inventoryService.isExists(productId)) {
            return  ResponseEntity.ok("Product recieved "); // If product exists, return 200 OK
        } else {
            return  ResponseEntity.status(HttpStatus.CREATED).body("New product created"); // If product doesn't exist, return 201 CREATED
        }
    }

    @PatchMapping("/reserve")
    public ResponseEntity<?> reserveProducts(
            @RequestBody @Valid List<InventoryRequest> requests
    ) {
        inventoryService.reserveProduct(requests);
        return  ResponseEntity.ok("Reservation done");
    }

    @PatchMapping("/release")
    public ResponseEntity<?> releaseProduct(
             @RequestBody @Valid List<InventoryRequest> request
    ){
        inventoryService.releaseOrder(request);
        return  ResponseEntity.ok("Order released successfully");
    }



    @PatchMapping("/cancel")
    public ResponseEntity<?> cancelOrders(
            @RequestBody @Valid List<InventoryRequest> requests
    ) {
        inventoryService.cancelOrder(requests);
        return  ResponseEntity.ok("Order cancelled");
    }
*/


}
