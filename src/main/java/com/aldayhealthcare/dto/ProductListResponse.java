package com.aldayhealthcare.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ProductListResponse {
    private boolean success;
    private int total;
    private int page;
    private int pages;
    private List<ProductResponse> data;
}