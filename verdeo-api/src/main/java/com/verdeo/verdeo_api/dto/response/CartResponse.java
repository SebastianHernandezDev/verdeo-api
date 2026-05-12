package com.verdeo.verdeo_api.dto.response;

import com.verdeo.verdeo_api.model.CartItem;

import java.util.List;
import java.util.UUID;

public class CartResponse {

    private UUID cartId;
    private List<CartItemResponse> items;
    private Double total;

    public CartResponse() {}

    public static CartResponse from(UUID cartId, List<CartItem> items) {
        CartResponse dto = new CartResponse();
        dto.cartId = cartId;
        dto.items  = items.stream().map(CartItemResponse::from).toList();
        dto.total  = dto.items.stream().mapToDouble(CartItemResponse::getSubtotal).sum();
        return dto;
    }

    public UUID getCartId()                  { return cartId; }
    public List<CartItemResponse> getItems() { return items; }
    public Double getTotal()                 { return total; }
}