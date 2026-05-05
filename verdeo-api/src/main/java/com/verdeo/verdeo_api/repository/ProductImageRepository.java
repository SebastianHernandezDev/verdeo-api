package com.verdeo.verdeo_api.repository;

import com.verdeo.verdeo_api.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

    // Todas las imágenes de un producto
    List<ProductImage> findByProductId(UUID productId);

    // Imagen principal del producto
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(UUID productId);

    //  Ordenadas (para galería)
    List<ProductImage> findByProductIdOrderBySortOrderAsc(UUID productId);
}