package com.verdeo.verdeo_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private boolean isPrimary = false;

    @Column
    private Integer sortOrder;

    public ProductImage() {}

    public ProductImage(Product product, String url, boolean isPrimary, Integer sortOrder) {
        this.product = product;
        this.url = url;
        this.isPrimary = isPrimary;
        this.sortOrder = sortOrder;
    }



    public UUID getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getUrl() {
        return url;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }


    public void setProduct(Product product) {
        this.product = product;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}