package com.verdeo.verdeo_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ProductImageRequest {

    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String url;

    private boolean isPrimary = false;

    private Integer sortOrder;

    public ProductImageRequest() {}

    public String getUrl()        { return url; }
    public boolean isPrimary()    { return isPrimary; }
    public Integer getSortOrder() { return sortOrder; }

    public void setUrl(String url)            { this.url = url; }
    public void setPrimary(boolean primary)   { isPrimary = primary; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}