package com.example.Catalog.Services;

import com.example.Catalog.DTO.ProductRequestDTO;
import com.example.Catalog.DTO.ProductResponseDTO;
import com.example.Catalog.Entities.Product;
import com.example.Catalog.Repository.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class ProductService {

    ProductRepo productRepo;
    ModelMapper modelMapper;

    @Autowired
    private ProductService(ProductRepo productRepo, ModelMapper modelMapper) {
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;

    }

    public ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO) {
        Product product = modelMapper.map(productRequestDTO, Product.class);

        productRepo.save(product);
        ProductResponseDTO productResponseDTO = modelMapper.map(product, ProductResponseDTO.class);
        return productResponseDTO;

    }

    public List<ProductResponseDTO> findAll() {
        return productRepo.findAll().stream().map(e -> {
            ProductResponseDTO productResponseDTO = modelMapper.map(e, ProductResponseDTO.class);
            return productResponseDTO;
        }).collect(Collectors.toList());
    }

    public ProductResponseDTO findbyId(Long id) {
        Optional<Product> product = Optional.ofNullable(productRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found ")));
        ProductResponseDTO productResponseDTO = modelMapper.map(product, ProductResponseDTO.class);
        return productResponseDTO;
    }

    public void delete(Long id) {
        productRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Product Not Found"));;
        productRepo.deleteById(id);
    }

    public ProductResponseDTO update(ProductRequestDTO productRequestDTO, Long id) {
        Optional<Product> productOptional = Optional.ofNullable(productRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Assure Not Found")));;
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            modelMapper.map(productRequestDTO, product);
            Product updatedProduct = productRepo.save(product);
            return modelMapper.map(updatedProduct, ProductResponseDTO.class);
        } else {
            throw new NoSuchElementException("Product with ID " + id + " not found.");
        }
    }
}
