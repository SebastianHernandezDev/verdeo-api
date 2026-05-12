package com.verdeo.verdeo_api.controller;

import com.verdeo.verdeo_api.dto.request.ProductImageRequest;
import com.verdeo.verdeo_api.dto.response.ProductImageResponse;
import com.verdeo.verdeo_api.service.ProductImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/images")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @GetMapping
    public ResponseEntity<List<ProductImageResponse>> getImages(@PathVariable UUID productId) {
        return ResponseEntity.ok(
                productImageService.getProductImages(productId)
                        .stream()
                        .map(ProductImageResponse::from)
                        .toList()
        );
    }

    @GetMapping("/primary")
    public ResponseEntity<ProductImageResponse> getPrimaryImage(@PathVariable UUID productId) {
        var image = productImageService.getPrimaryImage(productId);
        if (image == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ProductImageResponse.from(image));
    }

    @PostMapping
    public ResponseEntity<ProductImageResponse> addImage(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductImageRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ProductImageResponse.from(
                        productImageService.addImage(
                                productId,
                                request.getUrl(),
                                request.isPrimary(),
                                request.getSortOrder()
                        )
                )
        );
    }

    @PatchMapping("/{imageId}/primary")
    public ResponseEntity<ProductImageResponse> setPrimary(
            @PathVariable UUID productId,
            @PathVariable UUID imageId) {
        return ResponseEntity.ok(
                ProductImageResponse.from(productImageService.setPrimaryImage(productId, imageId))
        );
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID imageId) {
        productImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}