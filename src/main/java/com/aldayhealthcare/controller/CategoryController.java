package com.aldayhealthcare.controller;

import com.aldayhealthcare.model.Category;
import com.aldayhealthcare.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "https://alday-healthcare.netlify.app"}, maxAge = 3600)@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
}