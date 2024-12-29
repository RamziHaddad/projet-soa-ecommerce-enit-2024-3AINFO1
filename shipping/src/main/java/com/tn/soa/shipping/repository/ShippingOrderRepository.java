package com.tn.soa.shipping.repository;

import com.tn.soa.shipping.model.ShippingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShippingOrderRepository extends JpaRepository<ShippingOrder, UUID> {
    Optional<ShippingOrder> findByOrderId(UUID orderId);
}
