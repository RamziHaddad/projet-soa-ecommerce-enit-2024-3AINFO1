package com.example.Catalog.Controllers;

import com.example.Catalog.DTO.ProductDTO.ProductRequestDTO;
import com.example.Catalog.DTO.ProductDTO.ProductResponseDTO;
import com.example.Catalog.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("product")
public class ProductController {
    ProductService productService ;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService =productService;
    }
    @GetMapping("")
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        return new ResponseEntity<>(productService.findAll() , HttpStatus.OK) ;
    }
    @PostMapping("")
    public ResponseEntity<ProductResponseDTO> save(@RequestBody()ProductRequestDTO productRequestDTO) {
        ProductResponseDTO productResponseDTO = productService.saveProduct(productRequestDTO);
        return new ResponseEntity<>(productResponseDTO,HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable("id") UUID id){
        ProductResponseDTO productResponseDTO= productService.findbyId(id);
        return  ResponseEntity.ok(productResponseDTO);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?>delete(@PathVariable("id") UUID id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/id/{id}")
    public  ResponseEntity<ProductResponseDTO> update(@RequestBody() ProductRequestDTO productRequestDTO , @PathVariable("id")UUID id){
        ProductResponseDTO productResponseDTO=productService.update(productRequestDTO,id);
        return ResponseEntity.accepted().body(productResponseDTO);
    }
}
