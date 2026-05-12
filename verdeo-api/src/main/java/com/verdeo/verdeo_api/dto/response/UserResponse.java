package com.verdeo.verdeo_api.dto.response;

import com.verdeo.verdeo_api.model.Role;
import com.verdeo.verdeo_api.model.User;

import java.util.UUID;

public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private Role role;
    private boolean isActive;

    public UserResponse() {}

    public static UserResponse from(User user) {
        UserResponse dto = new UserResponse();
        dto.id       = user.getIdUser();
        dto.name     = user.getName();
        dto.email    = user.getEmail();
        dto.role     = user.getRole();
        dto.isActive = user.isActive();
        return dto;
    }

    public UUID getId()        { return id; }
    public String getName()    { return name; }
    public String getEmail()   { return email; }
    public Role getRole()      { return role; }
    public boolean isActive()  { return isActive; }
}