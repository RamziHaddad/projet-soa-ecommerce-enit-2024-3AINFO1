package com.soa_ecommerce.inventory.repository;

import com.soa_ecommerce.inventory.domain.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails, UUID> {
}
