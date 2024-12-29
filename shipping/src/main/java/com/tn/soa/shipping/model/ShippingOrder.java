package main.java.com.tn.soa.shipping.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class ShippingOrder {
    @Id
    private UUID orderId;
    private UUID customerId;
    private ShippingStatus status;
    private LocalDateTime scheduledDate;

    // Getters and setters
}
