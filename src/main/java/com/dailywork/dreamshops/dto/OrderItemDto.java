package com.dailywork.dreamshops.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private BigDecimal productPrice;
    private BigDecimal quantity;
    private BigDecimal price;
}
