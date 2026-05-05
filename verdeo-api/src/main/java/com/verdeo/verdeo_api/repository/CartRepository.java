package com.verdeo.verdeo_api.repository;

import com.verdeo.verdeo_api.model.Cart;
import com.verdeo.verdeo_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUser(User user);
}