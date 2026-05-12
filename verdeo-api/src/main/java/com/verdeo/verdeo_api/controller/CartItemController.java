package com.verdeo.verdeo_api.controller;

import com.verdeo.verdeo_api.model.Cart;
import com.verdeo.verdeo_api.model.CartItem;
import com.verdeo.verdeo_api.model.Product;
import com.verdeo.verdeo_api.service.CartItemService;
import com.verdeo.verdeo_api.service.CartService;
import com.verdeo.verdeo_api.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final ProductService productService;

    public CartItemController(CartItemService cartItemService,
                              CartService cartService,
                              ProductService productService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.productService = productService;
    }

    // GET items by userId (resuelve el cart internamente)
    @GetMapping
    public ResponseEntity<List<CartItem>> getItemsByUser(@RequestParam UUID userId) {
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(cartItemService.getItemsByCart(cart));
    }

    // GET items directamente por cartId
    @GetMapping("/by-cart/{cartId}")
    public ResponseEntity<List<CartItem>> getItemsByCartId(@PathVariable UUID cartId) {
        return ResponseEntity.ok(cartItemService.getItemsByCartId(cartId));
    }

    // POST agregar item (resuelve cart y product internamente)
    @PostMapping
    public ResponseEntity<CartItem> addItem(@RequestBody Map<String, Object> body) {
        UUID userId = UUID.fromString((String) body.get("userId"));
        UUID productId = UUID.fromString((String) body.get("productId"));
        int quantity = (int) body.get("quantity");

        Cart cart = cartService.getCart(userId);
        Product product = productService.getById(productId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartItemService.addItem(cart, product, quantity));
    }

    // PATCH actualizar cantidad
    @PatchMapping("/quantity")
    public ResponseEntity<CartItem> updateQuantity(@RequestBody Map<String, Object> body) {
        UUID cartId = UUID.fromString((String) body.get("cartId"));
        UUID productId = UUID.fromString((String) body.get("productId"));
        int quantity = (int) body.get("quantity");

        return ResponseEntity.ok(cartItemService.updateQuantity(cartId, productId, quantity));
    }

    // DELETE eliminar item específico
    @DeleteMapping
    public ResponseEntity<Void> removeItem(
            @RequestParam UUID cartId,
            @RequestParam UUID productId) {
        cartItemService.removeItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    // DELETE vaciar carrito por userId
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam UUID userId) {
        Cart cart = cartService.getCart(userId);
        cartItemService.clearCart(cart);
        return ResponseEntity.noContent().build();
    }

    // DELETE vaciar carrito por cartId
    @DeleteMapping("/clear/by-cart/{cartId}")
    public ResponseEntity<Void> clearCartById(@PathVariable UUID cartId) {
        cartItemService.clearCartById(cartId);
        return ResponseEntity.noContent().build();
    }

    // GET verificar si un producto existe en el carrito
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> existsItem(
            @RequestParam UUID userId,
            @RequestParam UUID productId) {
        Cart cart = cartService.getCart(userId);
        Product product = productService.getById(productId);
        boolean exists = cartItemService.existsItem(cart, product);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}