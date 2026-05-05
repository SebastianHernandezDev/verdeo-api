package com.verdeo.verdeo_api.repository;

import com.verdeo.verdeo_api.model.Cart;
import com.verdeo.verdeo_api.model.CartItem;
import com.verdeo.verdeo_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    // Obtener items por carrito
    List<CartItem> findByCart(Cart cart);

    // Obtener items por carrito
    List<CartItem> findByCartId(UUID cartId);

    //Buscar item específico
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // Alternativa por ID
    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);

    // Vaciar carrito
    void deleteByCart(Cart cart);

    void deleteByCartId(UUID cartId);

    //Ordenado
    List<CartItem> findByCartOrderByIdAsc(Cart cart);
}