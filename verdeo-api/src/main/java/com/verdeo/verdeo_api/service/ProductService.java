package com.verdeo.verdeo_api.service;

import com.verdeo.verdeo_api.exception.BadRequestException;
import com.verdeo.verdeo_api.exception.ResourceNotFoundException;
import com.verdeo.verdeo_api.model.Category;
import com.verdeo.verdeo_api.model.Product;
import com.verdeo.verdeo_api.model.ProductStatus;
import com.verdeo.verdeo_api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository,
                          CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Product> getByStatus(ProductStatus status) {
        return productRepository.findByStatus(status);
    }

    public List<Product> getByCategory(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getByCategoryAndStatus(UUID categoryId, ProductStatus status) {
        return productRepository.findByCategoryIdAndStatus(categoryId, status);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getAvailable() {
        return productRepository.findByStockGreaterThan(0);
    }

    public List<Product> getRecentByStatus(ProductStatus status) {
        return productRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public Product getById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    public Product create(Product product, UUID categoryId) {
        Category category = categoryService.getById(categoryId);
        product.setCategory(category);

        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.ACTIVO);
        }

        return productRepository.save(product);
    }

    public Product update(UUID id, Product updated, UUID categoryId) {
        Product existing = getById(id);
        Category category = categoryService.getById(categoryId);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setStock(updated.getStock());
        existing.setStatus(updated.getStatus());
        existing.setCategory(category);

        return productRepository.save(existing);
    }

    public void changeStatus(UUID id, ProductStatus status) {
        Product product = getById(id);
        product.setStatus(status);
        productRepository.save(product);
    }

    public void delete(UUID id) {
        Product product = getById(id);
        productRepository.delete(product);
    }
}