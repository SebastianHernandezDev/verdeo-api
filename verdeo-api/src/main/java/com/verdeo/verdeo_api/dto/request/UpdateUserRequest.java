package com.verdeo.verdeo_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateUserRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    public UpdateUserRequest() {}

    public String getName()  { return name; }
    public String getEmail() { return email; }

    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }
}