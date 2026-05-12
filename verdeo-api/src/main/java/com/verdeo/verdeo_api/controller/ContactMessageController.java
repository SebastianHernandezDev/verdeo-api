package com.verdeo.verdeo_api.controller;

import com.verdeo.verdeo_api.dto.response.ContactMessageResponse;
import com.verdeo.verdeo_api.model.ContactMessage;
import com.verdeo.verdeo_api.model.SenderType;
import com.verdeo.verdeo_api.service.ContactMessageService;
import com.verdeo.verdeo_api.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;
    private final EmailService emailService;

    public ContactMessageController(ContactMessageService contactMessageService,
                                    EmailService emailService) {
        this.contactMessageService = contactMessageService;
        this.emailService = emailService;
    }

    // Público: cualquier usuario puede enviar un mensaje
    @PostMapping
    public ResponseEntity<ContactMessageResponse> send(@RequestBody ContactMessage message) {
        ContactMessage saved = contactMessageService.send(message);
        emailService.sendContactNotification(saved);
        emailService.sendAutoReply(saved);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ContactMessageResponse.from(saved));
    }

    // Admin: listar con filtros opcionales
    @GetMapping
    public ResponseEntity<List<ContactMessageResponse>> getAll(
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) SenderType senderType) {

        List<ContactMessage> messages;

        if (senderType != null && read != null)  messages = contactMessageService.getBySenderTypeAndRead(senderType, read);
        else if (senderType != null)             messages = contactMessageService.getBySenderType(senderType);
        else if (read != null && read)           messages = contactMessageService.getRead();
        else if (read != null)                   messages = contactMessageService.getUnread();
        else                                     messages = contactMessageService.getAll();

        return ResponseEntity.ok(messages.stream().map(ContactMessageResponse::from).toList());
    }

    @GetMapping("/latest")
    public ResponseEntity<List<ContactMessageResponse>> getLatest() {
        return ResponseEntity.ok(
                contactMessageService.getLatest().stream().map(ContactMessageResponse::from).toList()
        );
    }

    @GetMapping("/count/unread")
    public ResponseEntity<Map<String, Long>> countUnread() {
        return ResponseEntity.ok(Map.of("unread", contactMessageService.countUnread()));
    }

    @GetMapping("/by-email")
    public ResponseEntity<List<ContactMessageResponse>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(
                contactMessageService.getByEmail(email).stream().map(ContactMessageResponse::from).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactMessageResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ContactMessageResponse.from(contactMessageService.getById(id)));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ContactMessageResponse> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(ContactMessageResponse.from(contactMessageService.markAsRead(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        contactMessageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}