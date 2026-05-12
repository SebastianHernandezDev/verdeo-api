package com.verdeo.verdeo_api.controller;

import com.verdeo.verdeo_api.dto.request.AddCartItemRequest;
import com.verdeo.verdeo_api.dto.request.DecreaseCartItemRequest;
import com.verdeo.verdeo_api.dto.response.CartResponse;
import com.verdeo.verdeo_api.model.Cart;
import com.verdeo.verdeo_api.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * userId se extrae del JWT automáticamente con @AuthenticationPrincipal.
 * El cliente ya NO pasa el userId por URL — el servidor lo saca del token.
 *
 * JwtFilter guarda el userId como "principal" en el SecurityContext:
 *   new UsernamePasswordAuthenticationToken(userId, null, authorities)
 *
 * @AuthenticationPrincipal lo recupera aquí como UUID.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // GET /api/cart
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UUID userId) {
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(CartResponse.from(cart.getId(), cartService.getItems(userId)));
    }

    // POST /api/cart/items
    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody AddCartItemRequest request) {

        cartService.addProduct(userId, request.getProductId(), request.getQuantity());
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(CartResponse.from(cart.getId(), cartService.getItems(userId)));
    }

    // PATCH /api/cart/items/decrease
    @PatchMapping("/items/decrease")
    public ResponseEntity<CartResponse> decreaseItem(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody DecreaseCartItemRequest request) {

        cartService.decreaseProduct(userId, request.getProductId(), request.getQuantity());
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(CartResponse.from(cart.getId(), cartService.getItems(userId)));
    }

    // DELETE /api/cart/items/{productId}
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID productId) {

        cartService.removeProduct(userId, productId);
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(CartResponse.from(cart.getId(), cartService.getItems(userId)));
    }

    // DELETE /api/cart
    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UUID userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}