package com.example.Catalog.Controllers;

import com.example.Catalog.DTO.ProductCategoryDTO.CategoryRequestDTO;
import com.example.Catalog.DTO.ProductCategoryDTO.CategoryResponseDTO;
import com.example.Catalog.DTO.ProductDTO.ProductRequestDTO;
import com.example.Catalog.DTO.ProductDTO.ProductResponseDTO;
import com.example.Catalog.Services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/category")
public class ProductCategoryController {

    ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryController(ProductCategoryService productCategoryService){
        this.productCategoryService=productCategoryService;
    }



    @GetMapping("")
    public ResponseEntity<List<CategoryResponseDTO>> getCategories(){
        return new ResponseEntity<>(productCategoryService.findAll(),HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<CategoryResponseDTO> save(@RequestBody()CategoryRequestDTO categoryRequestDTO){
        CategoryResponseDTO categoryResponseDTO=productCategoryService.saveCategory(categoryRequestDTO);
        return new ResponseEntity<>(categoryResponseDTO,HttpStatus.CREATED);
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryResponseDTO> findbyId(@PathVariable("id") UUID id){
        CategoryResponseDTO categoryResponseDTO=productCategoryService.findbyId(id);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?>delete(@PathVariable("id") UUID id){
        productCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }



    @PutMapping("/id/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@RequestBody() CategoryRequestDTO categoryRequestDTO, @PathVariable("id") UUID id){
        CategoryResponseDTO categoryResponseDTO=productCategoryService.update(categoryRequestDTO,id);
        return ResponseEntity.accepted().body(categoryResponseDTO);
    }

}
