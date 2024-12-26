package com.soa_ecommerce.inventory.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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
    private boolean released;
    private boolean cancelled;


    @OneToMany(mappedBy = "reservation")
    private List<ReservationDetails> reservationsDetails;

}