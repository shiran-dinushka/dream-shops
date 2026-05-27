package com.dailywork.dreamshops.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer inventory;
    private String description;
    private CategoryDto category;
    private List<ImageDto> images;
}
