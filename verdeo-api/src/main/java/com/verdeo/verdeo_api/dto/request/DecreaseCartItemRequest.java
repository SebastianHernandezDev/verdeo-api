package com.verdeo.verdeo_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class DecreaseCartItemRequest {

    @NotNull(message = "El productId es obligatorio")
    private UUID productId;

    @Min(value = 1, message = "La cantidad mínima es 1")
    private int quantity;

    public DecreaseCartItemRequest() {}

    public UUID getProductId() { return productId; }
    public int getQuantity()   { return quantity; }

    public void setProductId(UUID productId) { this.productId = productId; }
    public void setQuantity(int quantity)    { this.quantity = quantity; }
}