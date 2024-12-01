package com.example.demo.Repository;

import com.example.demo.Models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    // Rechercher une promotion par ID produit
    Optional<Promotion> findByProductId(Long productId);
}
