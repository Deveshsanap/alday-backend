package com.aldayhealthcare.service;

import com.aldayhealthcare.dto.ProductResponse;
import com.aldayhealthcare.exception.ResourceNotFoundException;
import com.aldayhealthcare.model.Product;
import com.aldayhealthcare.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponse> getAllProductsFormatted() {
        return productRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public ProductResponse getSingleProductFormatted(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public ProductResponse updateProduct(Long id, ProductResponse request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));

        // Map the incoming fields from React
        if (request.getName() != null) product.setTitle(request.getName());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getMrp() != null) product.setMrp(request.getMrp());
        
        product.setStockQuantity(request.getCountInStock());
        product.setBestSeller(request.isBestSeller());
        
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getImage() != null) product.setImageUrl(request.getImage());

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        
        response.setId("prod-" + product.getId());
        response.setName(product.getTitle()); 
        response.setTitle(product.getTitle());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setMrp(product.getMrp());
        
        // Mapped to exactly match ProductResponse.java variables
        response.setCountInStock(product.getStockQuantity());
        response.setImage(product.getImageUrl());
        response.setBestSeller(product.isBestSeller());
        response.setStatus(product.getStatus());
        
        return response;
    }
}