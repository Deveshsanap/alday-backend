package com.aldayhealthcare.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
	// Change this from 'private Long productId;'
	private String productId;
	private Integer quantity;
}