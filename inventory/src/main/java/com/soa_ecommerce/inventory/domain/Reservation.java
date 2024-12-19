package com.soa_ecommerce.inventory.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reservationId;
    private UUID orderId;
    private Integer quantityReserved;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
