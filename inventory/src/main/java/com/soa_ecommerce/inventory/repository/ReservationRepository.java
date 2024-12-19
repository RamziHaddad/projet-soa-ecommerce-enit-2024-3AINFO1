package com.soa_ecommerce.inventory.repository;

import com.soa_ecommerce.inventory.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    public Optional<Reservation> findByOrderId(UUID orderId);
}
