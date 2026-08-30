package com.aldayhealthcare.dto;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderItemResponse {
	private String productId;
	private String title;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}