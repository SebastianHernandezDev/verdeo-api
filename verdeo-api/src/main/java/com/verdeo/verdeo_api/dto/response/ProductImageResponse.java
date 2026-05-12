package com.verdeo.verdeo_api.dto.response;

import com.verdeo.verdeo_api.model.ProductImage;

import java.util.UUID;

public class ProductImageResponse {

    private UUID id;
    private String url;
    private boolean isPrimary;
    private Integer sortOrder;

    public ProductImageResponse() {}

    public static ProductImageResponse from(ProductImage image) {
        ProductImageResponse dto = new ProductImageResponse();
        dto.id        = image.getId();
        dto.url       = image.getUrl();
        dto.isPrimary = image.isPrimary();
        dto.sortOrder = image.getSortOrder();
        return dto;
    }

    public UUID getId()        { return id; }
    public String getUrl()     { return url; }
    public boolean isPrimary() { return isPrimary; }
    public Integer getSortOrder() { return sortOrder; }
}