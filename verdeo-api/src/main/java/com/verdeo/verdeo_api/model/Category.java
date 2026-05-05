package com.verdeo.verdeo_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table (name = "category")

public class Category {

    @Id
    @GeneratedValue
    private UUID idCategory;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true)
    private String slug;

    @Column(nullable = false)
    private boolean active = true;

    public Category() {
    }

    public Category(UUID idCategory, String name, String description, String slug, boolean active) {
        this.idCategory = idCategory;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.active = active;
    }

    public UUID getIdCategory() {
        return idCategory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
