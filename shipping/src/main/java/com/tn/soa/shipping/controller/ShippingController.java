package main.java.com.tn.soa.shipping.controller;

import com.tn.soa.shipping.dto.ShippingOrder;
import com.tn.soa.shipping.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping("/schedule")
    public ShippingOrder scheduleShipping(@RequestBody ShippingOrder shippingOrder) {
        return shippingService.scheduleShipping(shippingOrder.getOrderId(),
                shippingOrder.getCustomerId(),
                shippingOrder.getProducts());
    }

    @PatchMapping("/status/{orderId}")
    public boolean updateShippingStatus(@PathVariable UUID orderId, @RequestParam String status) {
        return shippingService.updateShippingStatus(orderId, status);
    }
}
