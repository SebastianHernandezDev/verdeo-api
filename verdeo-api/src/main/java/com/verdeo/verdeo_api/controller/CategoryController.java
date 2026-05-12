package com.verdeo.verdeo_api.controller;

import com.verdeo.verdeo_api.dto.response.CategoryResponse;
import com.verdeo.verdeo_api.model.Category;
import com.verdeo.verdeo_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        List<CategoryResponse> categories = categoryService.getAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(CategoryResponse.from(categoryService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CategoryResponse.from(categoryService.create(category)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable UUID id,
            @RequestBody Category updated) {
        return ResponseEntity.ok(CategoryResponse.from(categoryService.update(id, updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}