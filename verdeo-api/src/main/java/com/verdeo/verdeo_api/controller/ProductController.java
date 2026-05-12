package com.verdeo.verdeo_api.controller;

import com.verdeo.verdeo_api.dto.request.ProductRequest;
import com.verdeo.verdeo_api.dto.response.ProductResponse;
import com.verdeo.verdeo_api.model.Product;
import com.verdeo.verdeo_api.model.ProductStatus;
import com.verdeo.verdeo_api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean available) {

        List<Product> products;

        if (name != null)                          products = productService.searchByName(name);
        else if (available != null && available)   products = productService.getAvailable();
        else if (categoryId != null && status != null) products = productService.getByCategoryAndStatus(categoryId, status);
        else if (categoryId != null)               products = productService.getByCategory(categoryId);
        else if (status != null)                   products = productService.getByStatus(status);
        else                                       products = productService.getAll();

        return ResponseEntity.ok(products.stream().map(ProductResponse::from).toList());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ProductResponse>> getRecent(@RequestParam ProductStatus status) {
        return ResponseEntity.ok(
                productService.getRecentByStatus(status).stream().map(ProductResponse::from).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ProductResponse.from(productService.getById(id)));
    }

    // categoryId ahora viene dentro del body (ProductRequest), no como @RequestParam
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        Product product = toEntity(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductResponse.from(productService.create(product, request.getCategoryId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequest request) {
        Product product = toEntity(request);
        return ResponseEntity.ok(ProductResponse.from(productService.update(id, product, request.getCategoryId())));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable UUID id,
            @RequestParam ProductStatus status) {
        productService.changeStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ──────────────────────────────────────────
    // Helper: ProductRequest → Product entity
    // ──────────────────────────────────────────
    private Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setStatus(request.getStatus());
        return product;
    }
}