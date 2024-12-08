package com.example.Catalog.Services;

import com.example.Catalog.DTO.ProductCategoryDTO.CategoryRequestDTO;
import com.example.Catalog.DTO.ProductCategoryDTO.CategoryResponseDTO;
import com.example.Catalog.DTO.ProductDTO.ProductRequestDTO;
import com.example.Catalog.DTO.ProductDTO.ProductResponseDTO;
import com.example.Catalog.Entities.Product;
import com.example.Catalog.Entities.ProductCategory;
import com.example.Catalog.Repository.ProductCategoryRepo;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {

    ProductCategoryRepo productCategoryRepo;
    ModelMapper modelMapper;

    @Autowired
    private ProductCategoryService(ProductCategoryRepo productCategoryRepo, ModelMapper modelMapper){
        this.productCategoryRepo=productCategoryRepo;
        this.modelMapper=modelMapper;
    }



    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO){
        ProductCategory productCategory=modelMapper.map(categoryRequestDTO,ProductCategory.class);
        productCategoryRepo.save(productCategory);
        CategoryResponseDTO categoryResponseDTO=modelMapper.map(productCategory,CategoryResponseDTO.class);
        return categoryResponseDTO;
    }



    public List<CategoryResponseDTO> findAll(){
        return productCategoryRepo.findAll().stream().map(e ->{
            CategoryResponseDTO categoryResponseDTO= modelMapper.map(e,CategoryResponseDTO.class);
            return categoryResponseDTO;
        }).collect(Collectors.toList());
    }


    public CategoryResponseDTO findbyId(UUID id){
                Optional<ProductCategory> productCategory=Optional.ofNullable(productCategoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found")));
                CategoryResponseDTO categoryResponseDTO=modelMapper.map(productCategory,CategoryResponseDTO.class);
                return  categoryResponseDTO;
    }



    public void delete(UUID id){
        productCategoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Product Not Found"));
        productCategoryRepo.deleteById(id);
    }

    public CategoryResponseDTO update(CategoryRequestDTO categoryRequestDTO , UUID id){
        Optional<ProductCategory> productCategoryOptional=Optional.ofNullable(productCategoryRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Category not found")));
        if (productCategoryOptional.isPresent()){
            ProductCategory productCategory= productCategoryOptional.get();
            modelMapper.map(categoryRequestDTO,productCategory);
            ProductCategory updatedCategory= productCategoryRepo.save(productCategory);
            return modelMapper.map(updatedCategory,CategoryResponseDTO.class);
        }else{
            throw new NoSuchElementException("Category with ID " + id + " not found.");

        }
    }
}
