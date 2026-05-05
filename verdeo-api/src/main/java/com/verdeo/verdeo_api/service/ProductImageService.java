package com.verdeo.verdeo_api.service;

import com.verdeo.verdeo_api.model.Product;
import com.verdeo.verdeo_api.model.ProductImage;
import com.verdeo.verdeo_api.repository.ProductImageRepository;
import com.verdeo.verdeo_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    public ProductImageService(ProductImageRepository productImageRepository,
                               ProductRepository productRepository) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    // =========================
    // OBTENER TODAS LAS IMÁGENES
    // =========================
    public List<ProductImage> getProductImages(UUID productId) {
        return productImageRepository.findByProductIdOrderBySortOrderAsc(productId);
    }

    // =========================
    // IMAGEN PRINCIPAL
    // =========================
    public ProductImage getPrimaryImage(UUID productId) {
        return productImageRepository.findByProductIdAndIsPrimaryTrue(productId)
                .orElse(null);
    }

    // =========================
    // AGREGAR IMAGEN
    // =========================
    public ProductImage addImage(UUID productId, String url, boolean isPrimary, Integer sortOrder) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Si es primaria, desactivar otras
        if (isPrimary) {
            unsetPrimaryImages(productId);
        }

        ProductImage image = new ProductImage(product, url, isPrimary, sortOrder);

        return productImageRepository.save(image);
    }

    // =========================
    // CAMBIAR IMAGEN PRINCIPAL
    // =========================
    public ProductImage setPrimaryImage(UUID productId, UUID imageId) {

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        if (!image.getProduct().getIdProduct().equals(productId)) {
            throw new RuntimeException("La imagen no pertenece al producto");
        }

        unsetPrimaryImages(productId);

        image.setPrimary(true);

        return productImageRepository.save(image);
    }

    // =========================
    // ELIMINAR IMAGEN
    // =========================
    public void deleteImage(UUID imageId) {

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        productImageRepository.delete(image);
    }

    // =========================
    // LIMPIAR PRIMARY
    // =========================
    private void unsetPrimaryImages(UUID productId) {

        List<ProductImage> images = productImageRepository.findByProductId(productId);

        for (ProductImage img : images) {
            if (img.isPrimary()) {
                img.setPrimary(false);
                productImageRepository.save(img);
            }
        }
    }
}