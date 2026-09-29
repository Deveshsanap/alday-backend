package com.aldayhealthcare.controller;

import com.aldayhealthcare.dto.ProductResponse;
import com.aldayhealthcare.model.Product;
import com.aldayhealthcare.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "https://alday-healthcare.netlify.app"}, maxAge = 3600)@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProductsFormatted());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getSingleProduct(@PathVariable String id) {
        Long cleanId = Long.valueOf(id.replace("prod-", ""));
        return ResponseEntity.ok(productService.getSingleProductFormatted(cleanId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id, 
            @RequestBody ProductResponse productRequest) { // Accept the DTO instead of the Entity
        
        Long cleanId = Long.valueOf(id.replace("prod-", ""));
        return ResponseEntity.ok(productService.updateProduct(cleanId, productRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        Long cleanId = Long.valueOf(id.replace("prod-", ""));
        productService.deleteProduct(cleanId);
        return ResponseEntity.ok().body("Product deleted successfully");
    }
}