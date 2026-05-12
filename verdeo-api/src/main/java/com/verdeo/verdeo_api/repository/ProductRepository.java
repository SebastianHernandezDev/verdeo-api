package com.verdeo.verdeo_api.repository;

import com.verdeo.verdeo_api.model.Product;
import com.verdeo.verdeo_api.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByCategory_IdCategory(UUID categoryId);

    List<Product> findByCategory_IdCategoryAndStatus(UUID categoryId, ProductStatus status);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByStockGreaterThan(Integer stock);

    List<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status);
}