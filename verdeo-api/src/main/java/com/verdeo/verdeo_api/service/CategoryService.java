package com.verdeo.verdeo_api.service;

import com.verdeo.verdeo_api.exception.BadRequestException;
import com.verdeo.verdeo_api.exception.ResourceNotFoundException;
import com.verdeo.verdeo_api.model.Category;
import com.verdeo.verdeo_api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public Category getById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada"));
    }

    public Category create(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new BadRequestException("Ya existe una categoría con ese nombre");
        }
        return categoryRepository.save(category);
    }

    public Category update(UUID id, Category updated) {
        Category existing = getById(id);

        if (!existing.getName().equalsIgnoreCase(updated.getName()) &&
                categoryRepository.existsByNameIgnoreCase(updated.getName())) {
            throw new BadRequestException("Ya existe una categoría con ese nombre");
        }

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return categoryRepository.save(existing);
    }

    public void delete(UUID id) {
        Category category = getById(id);
        categoryRepository.delete(category);
    }
}