package com.verdeo.verdeo_api.dto.request;

import com.verdeo.verdeo_api.model.ProductStatus;
import jakarta.validation.constraints.*;

import java.util.UUID;

public class ProductRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double price;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private ProductStatus status;

    @NotNull(message = "La categoría es obligatoria")
    private UUID categoryId;

    public ProductRequest() {}

    public String getName()          { return name; }
    public String getDescription()   { return description; }
    public Double getPrice()         { return price; }
    public Integer getStock()        { return stock; }
    public ProductStatus getStatus() { return status; }
    public UUID getCategoryId()      { return categoryId; }

    public void setName(String name)                  { this.name = name; }
    public void setDescription(String description)    { this.description = description; }
    public void setPrice(Double price)                { this.price = price; }
    public void setStock(Integer stock)               { this.stock = stock; }
    public void setStatus(ProductStatus status)       { this.status = status; }
    public void setCategoryId(UUID categoryId)        { this.categoryId = categoryId; }
}