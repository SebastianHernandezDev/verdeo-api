package com.verdeo.verdeo_api.service;

import com.verdeo.verdeo_api.exception.ResourceNotFoundException;
import com.verdeo.verdeo_api.model.ContactMessage;
import com.verdeo.verdeo_api.model.SenderType;
import com.verdeo.verdeo_api.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    public ContactMessage send(ContactMessage message) {
        return contactMessageRepository.save(message);
    }

    public List<ContactMessage> getAll() {
        return contactMessageRepository.findAll();
    }

    public List<ContactMessage> getUnread() {
        return contactMessageRepository.findByReadOrderByCreatedAtDesc(false);
    }

    public List<ContactMessage> getRead() {
        return contactMessageRepository.findByReadOrderByCreatedAtDesc(true);
    }

    public List<ContactMessage> getBySenderType(SenderType senderType) {
        return contactMessageRepository.findBySenderType(senderType);
    }

    public List<ContactMessage> getBySenderTypeAndRead(SenderType senderType, boolean read) {
        return contactMessageRepository.findBySenderTypeAndReadOrderByCreatedAtDesc(senderType, read);
    }

    public List<ContactMessage> getByEmail(String email) {
        return contactMessageRepository.findByEmailIgnoreCase(email);
    }

    public long countUnread() {
        return contactMessageRepository.countByReadFalse();
    }

    public List<ContactMessage> getLatest() {
        return contactMessageRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public ContactMessage getById(UUID id) {
        return contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje no encontrado"));
    }

    public ContactMessage markAsRead(UUID id) {
        ContactMessage message = getById(id);
        message.markAsRead();
        return contactMessageRepository.save(message);
    }

    public void delete(UUID id) {
        ContactMessage message = getById(id);
        contactMessageRepository.delete(message);
    }
}