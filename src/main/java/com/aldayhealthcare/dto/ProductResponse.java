package com.aldayhealthcare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductResponse {
    @JsonProperty("_id")
    private String id; // Changed from Long to String for the "prod-" prefix
    private String productId;
    private String name;
    private String title;
    private String subtitle;
    private String vendor;
    private List<String> category;
    private String concern;
    private BigDecimal price;
    private BigDecimal mrp;
    private boolean sale;
    private double rating;
    private int reviewCount;
    private String image;
    private List<String> images;
    private String description;
    private List<Object> keyActives;
    private List<Object> ritual;
    private List<Object> fullIngredients;
    private String tag;
    private boolean isActive;
    private String createdAt;
    private String updatedAt;
    @JsonProperty("__v")
    private int v;
    private boolean bestSeller;
    private int countInStock;
    private List<Object> reviews;
    private String status; 
}