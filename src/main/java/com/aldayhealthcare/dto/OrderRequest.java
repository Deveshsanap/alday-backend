package com.aldayhealthcare.dto;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    
    // Map natively catches JSON objects without needing Jackson imports
    private Map<String, Object> shippingAddress; 
    
    private List<OrderItemRequest> items;
}