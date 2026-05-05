package com.verdeo.verdeo_api.service;

import com.verdeo.verdeo_api.exception.BadRequestException;
import com.verdeo.verdeo_api.exception.ResourceNotFoundException;
import com.verdeo.verdeo_api.model.*;
import com.verdeo.verdeo_api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // GET OR CREATE CART
    // =========================
    @Transactional
    public Cart getCart(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }


    // ADD PRODUCT

    @Transactional
    public void addProduct(UUID userId, UUID productId, int quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Cantidad inválida");
        }

        Cart cart = getCart(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (product.getStock() != null && product.getStock() < quantity) {
            throw new BadRequestException("Stock insuficiente");
        }

        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (item != null) {
            int newQuantity = item.getQuantity() + quantity;

            if (product.getStock() != null && newQuantity > product.getStock()) {
                throw new BadRequestException("Supera el stock disponible");
            }

            item.setQuantity(newQuantity);
            cartItemRepository.save(item); // 🔥 importante
        } else {
            CartItem newItem = new CartItem(cart, product, quantity);
            cartItemRepository.save(newItem);
        }
    }


    // DECREASE PRODUCT

    @Transactional
    public void decreaseProduct(UUID userId, UUID productId, int quantity) {

        if (quantity <= 0) {
            throw new BadRequestException("Cantidad inválida");
        }

        Cart cart = getCart(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new BadRequestException("El producto no está en el carrito"));

        int newQuantity = item.getQuantity() - quantity;

        if (newQuantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        }
    }


    // REMOVE PRODUCT

    @Transactional
    public void removeProduct(UUID userId, UUID productId) {

        Cart cart = getCart(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        CartItem item = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new BadRequestException("El producto no está en el carrito"));

        cartItemRepository.delete(item);
    }


    // CLEAR CART

    @Transactional
    public void clearCart(UUID userId) {
        Cart cart = getCart(userId);
        cartItemRepository.deleteByCart(cart);
    }


    // GET ITEMS

    public List<CartItem> getItems(UUID userId) {
        return cartItemRepository.findByCart(getCart(userId));
    }


    // TOTAL

    public double getTotal(UUID userId) {
        return getItems(userId).stream()
                .mapToDouble(item ->
                        item.getProduct().getPrice() * item.getQuantity()
                )
                .sum();
    }
}