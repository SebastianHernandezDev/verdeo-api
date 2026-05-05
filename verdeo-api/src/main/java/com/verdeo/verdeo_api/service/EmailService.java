package com.verdeo.verdeo_api.service;

import com.verdeo.verdeo_api.model.ContactMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    // CORE EMAIL SENDER

    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("no-reply@verdeo.com");

        mailSender.send(message);
    }


    // TEMPLATE ENGINE SIMPLE

    private String applyTemplate(String template, Map<String, String> vars) {

        String result = template;

        for (Map.Entry<String, String> entry : vars.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return result;
    }


    // CONTACT NOTIFICATION (ADMIN)

    public void sendContactNotification(ContactMessage msg) {

        String template = """
                🌿 VERDEO - Nuevo mensaje recibido

                👤 Cliente: {{name}}
                📧 Email: {{email}}
                🏷 Tipo: {{type}}

                💬 Mensaje:
                "{{message}}"

                ⚡ Revisa este mensaje en el panel de administración.
                """;

        String body = applyTemplate(template, Map.of(
                "name", msg.getName(),
                "email", msg.getEmail(),
                "type", msg.getSenderType().toString(),
                "message", msg.getMessage()
        ));

        String subject = "🌿 Nuevo contacto de " + msg.getName();

        sendEmail("admin@verdeo.com", subject, body);
    }


    // AUTO RESPUESTA DINÁMICA

    public void sendAutoReply(ContactMessage msg) {

        String[] greetings = {
                "Hola",
                "¡Qué gusto saludarte",
                "Hey",
                "Hola 👋"
        };

        String[] responses = {
                "hemos recibido tu mensaje y ya lo estamos revisando.",
                "tu mensaje llegó correctamente y pronto te responderemos.",
                "ya tenemos tu solicitud en nuestro sistema.",
                "estamos analizando tu mensaje en VERDEO "
        };

        String greeting = greetings[(int) (Math.random() * greetings.length)];
        String response = responses[(int) (Math.random() * responses.length)];

        String template = """
                {{greeting}} {{name}},

                {{response}}

                Mensaje recibido:
                "{{message}}"

                 Tiempo estimado de respuesta: 24 - 48 horas

                Gracias por confiar en VERDEO 🍃
                """;

        String body = applyTemplate(template, Map.of(
                "greeting", greeting,
                "name", msg.getName(),
                "response", response,
                "message", msg.getMessage()
        ));

        String subject = "Hemos recibido tu mensaje 🌿";

        sendEmail(msg.getEmail(), subject, body);
    }


    // EMAIL FUTURO (REUTILIZABLE)

    public void sendCustomEmail(String to, String name, String messageType, String customMessage) {

        String template = """
                VERDEO - Notificación

                Hola {{name}},

                {{message}}

                Tipo de notificación: {{type}}

                Saludos,
                Equipo VERDEO
                """;

        String body = applyTemplate(template, Map.of(
                "name", name,
                "message", customMessage,
                "type", messageType
        ));

        sendEmail(to, "Notificación VERDEO 🌿", body);
    }
}