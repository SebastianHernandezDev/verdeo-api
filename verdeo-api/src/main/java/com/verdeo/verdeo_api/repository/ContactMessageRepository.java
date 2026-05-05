package com.verdeo.verdeo_api.repository;

import com.verdeo.verdeo_api.model.ContactMessage;
import com.verdeo.verdeo_api.model.SenderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, UUID> {

    //  Mensajes leídos / no leídos
    List<ContactMessage> findByReadOrderByCreatedAtDesc(boolean read);

    //  Por tipo de remitente
    List<ContactMessage> findBySenderType(SenderType senderType);

    //  Filtro combinado (admin)
    List<ContactMessage> findBySenderTypeAndReadOrderByCreatedAtDesc(
            SenderType senderType,
            boolean read
    );

    //  Buscar por email
    List<ContactMessage> findByEmailIgnoreCase(String email);

    //  Contador de no leídos
    long countByReadFalse();

    //  Últimos mensajes
    List<ContactMessage> findTop5ByOrderByCreatedAtDesc();
}