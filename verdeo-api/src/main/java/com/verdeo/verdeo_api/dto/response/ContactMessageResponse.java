package com.verdeo.verdeo_api.dto.response;

import com.verdeo.verdeo_api.model.ContactMessage;
import com.verdeo.verdeo_api.model.SenderType;

import java.time.LocalDateTime;
import java.util.UUID;

public class ContactMessageResponse {

    private UUID id;
    private String name;
    private String email;
    private String message;
    private SenderType senderType;
    private boolean read;
    private LocalDateTime createdAt;

    public ContactMessageResponse() {}

    public static ContactMessageResponse from(ContactMessage msg) {
        ContactMessageResponse dto = new ContactMessageResponse();
        dto.id         = msg.getId();
        dto.name       = msg.getName();
        dto.email      = msg.getEmail();
        dto.message    = msg.getMessage();
        dto.senderType = msg.getSenderType();
        dto.read       = msg.isRead();
        dto.createdAt  = msg.getCreatedAt();
        return dto;
    }

    public UUID getId()              { return id; }
    public String getName()          { return name; }
    public String getEmail()         { return email; }
    public String getMessage()       { return message; }
    public SenderType getSenderType(){ return senderType; }
    public boolean isRead()          { return read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}