package com.verdeo.verdeo_api.dto.response;

import com.verdeo.verdeo_api.model.Category;

import java.util.UUID;

public class CategoryResponse {

    private UUID id;
    private String name;
    private String description;

    public CategoryResponse() {}

    public static CategoryResponse from(Category category) {
        CategoryResponse dto = new CategoryResponse();
        dto.id          = category.getIdCategory();
        dto.name        = category.getName();
        dto.description = category.getDescription();
        return dto;
    }

    public UUID getId()          { return id; }
    public String getName()      { return name; }
    public String getDescription() { return description; }
}