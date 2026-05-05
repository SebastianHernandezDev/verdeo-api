package com.verdeo.verdeo_api.service;


import com.verdeo.verdeo_api.model.Cart;
import com.verdeo.verdeo_api.model.CartItem;
import com.verdeo.verdeo_api.model.Product;
import com.verdeo.verdeo_api.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository){
        this.cartItemRepository = cartItemRepository;
    }

    // obtener items

    public List<CartItem> getItemsByCart(Cart cart){
        return cartItemRepository.findByCartOrderByIdAsc(cart);

    }
    // por id
    public List<CartItem> getItemsByCartId(UUID cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Transactional
    public CartItem addItem(Cart cart, Product product, int quantity) {

        return cartItemRepository
                .findByCartAndProduct(cart, product)
                .map(existingItem -> {
                    existingItem.increaseQuantity(quantity);
                    return cartItemRepository.save(existingItem);
                })
                .orElseGet(() -> {
                    CartItem newItem = new CartItem(cart, product, quantity);
                    return cartItemRepository.save(newItem);
                });
    }

    // actualizar
    @Transactional
    public CartItem updateQuantity(UUID cartId, UUID productId, int quantity) {

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));

        item.setQuantity(quantity);

        return cartItemRepository.save(item);
    }

    // eliminar item
    @Transactional
    public void removeItem(UUID cartId, UUID productId) {

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        cartItemRepository.delete(item);
    }


    // Vaciar carro

    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteByCart(cart);
    }

    public void clearCartById(UUID cartId) {
        cartItemRepository.deleteByCartId(cartId);
    }



    public boolean existsItem(Cart cart, Product product) {
        return cartItemRepository.findByCartAndProduct(cart, product).isPresent();
    }
}
