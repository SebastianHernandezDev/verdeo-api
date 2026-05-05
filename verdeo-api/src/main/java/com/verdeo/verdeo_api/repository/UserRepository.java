package com.verdeo.verdeo_api.repository;

import com.verdeo.verdeo_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {


    Optional<User> findByEmailIgnoreCaseAndIsActiveTrue(String email);


    Optional<User> findByEmailIgnoreCase(String email);


    boolean existsByEmailIgnoreCase(String email);
}