package com.example.Catalog.Repository;

import com.example.Catalog.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {

}
