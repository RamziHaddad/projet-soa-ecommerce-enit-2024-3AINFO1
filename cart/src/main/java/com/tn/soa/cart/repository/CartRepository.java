package com.tn.soa.cart.repository;

import com.tn.soa.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;


// This interface will automatically provide common methods like findById(), save(), and deleteById()
// You can add custom query methods if needed in the future
public interface CartRepository extends JpaRepository<Cart, UUID> {

    List<Cart> findByCustomerId(UUID customerId);
}
