package main.java.com.tn.soa.shipping.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class CustomerAddress {
    @Id
    private UUID id;
    private UUID customerId;
    private String address;

    // Getters and setters
}
