package com.verdeo.verdeo_api.dto.response;

import com.verdeo.verdeo_api.model.CartItem;

import java.util.UUID;

public class CartItemResponse {

    private UUID productId;
    private String productName;
    private Double unitPrice;
    private int quantity;
    private Double subtotal;

    public CartItemResponse() {}

    public static CartItemResponse from(CartItem item) {
        CartItemResponse dto = new CartItemResponse();
        dto.productId   = item.getProduct().getIdProduct();
        dto.productName = item.getProduct().getName();
        dto.unitPrice   = item.getProduct().getPrice();
        dto.quantity    = item.getQuantity();
        dto.subtotal    = item.getProduct().getPrice() * item.getQuantity();
        return dto;
    }

    public UUID getProductId()      { return productId; }
    public String getProductName()  { return productName; }
    public Double getUnitPrice()    { return unitPrice; }
    public int getQuantity()        { return quantity; }
    public Double getSubtotal()     { return subtotal; }
}