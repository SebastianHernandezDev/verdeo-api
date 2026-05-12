package com.verdeo.verdeo_api.dto.response;

import com.verdeo.verdeo_api.model.Product;
import com.verdeo.verdeo_api.model.ProductStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductResponse {

    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private ProductStatus status;
    private CategoryResponse category;
    private LocalDateTime createdAt;

    public ProductResponse() {}

    public static ProductResponse from(Product product) {
        ProductResponse dto = new ProductResponse();
        dto.id          = product.getIdProduct();  // ajusta si tu getter es distinto
        dto.name        = product.getName();
        dto.description = product.getDescription();
        dto.price       = product.getPrice();
        dto.stock       = product.getStock();
        dto.status      = product.getStatus();
        dto.createdAt   = product.getCreatedAt();
        if (product.getCategory() != null) {
            dto.category = CategoryResponse.from(product.getCategory());
        }
        return dto;
    }

    public UUID getId()                   { return id; }
    public String getName()               { return name; }
    public String getDescription()        { return description; }
    public Double getPrice()              { return price; }
    public Integer getStock()             { return stock; }
    public ProductStatus getStatus()      { return status; }
    public CategoryResponse getCategory() { return category; }
    public LocalDateTime getCreatedAt()   { return createdAt; }
}