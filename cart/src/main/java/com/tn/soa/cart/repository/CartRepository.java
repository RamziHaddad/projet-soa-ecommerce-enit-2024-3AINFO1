package com.tn.soa.cart.repository;

import com.tn.soa.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;



public interface CartRepository extends JpaRepository<Cart, UUID> {

    List<Cart> findByCustomerId(UUID customerId);
}
