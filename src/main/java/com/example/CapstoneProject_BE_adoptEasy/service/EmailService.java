package com.example.CapstoneProject_BE_adoptEasy.service;

import com.example.CapstoneProject_BE_adoptEasy.payload.ContactRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail; // Email del gestore

    @Autowired
    private JavaMailSender javaMailSender;

    // Metodo per inviare una email di contatto
    public void sendContactEmail(ContactRequest contactRequest) throws MessagingException {
        try {
            // 1. Invia email al gestore (quella definita nel properties)
            sendEmailToAdmin(contactRequest);

            // 2. Invia email di conferma all'utente
            sendConfirmationToUser(contactRequest);
        } catch (MessagingException e) {
            throw new MessagingException("Errore nell'invio delle email", e);
        }
    }

    // Metodo per inviare l'email al gestore
    private void sendEmailToAdmin(ContactRequest contactRequest) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true per abilitare HTML

        // L'email del mittente è quella dell'utente
        helper.setFrom(contactRequest.getEmail());
        helper.setTo(fromEmail);  // Email del gestore

        helper.setSubject("Nuova richiesta di contatto da " + contactRequest.getName());

        // Corpo dell'email in HTML
        String body = "<html>" +
                "<body style=\"font-family: Arial, sans-serif;\">" +
                "<h2 style=\"color: #1D68A7;\">Nuova richiesta di contatto ricevuta!</h2>" +
                "<p style=\"font-size: 16px; color: #333;\">Ciao,<br>Hai appena ricevuto una nuova richiesta di contatto da parte di un utente.</p>" +
                "<p><strong style=\"color: #2A2A2A;\">Nome:</strong> " + contactRequest.getName() + "</p>" +
                "<p><strong style=\"color: #2A2A2A;\">Email:</strong> " + contactRequest.getEmail() + "</p>" +
                "<p><strong style=\"color: #2A2A2A;\">Messaggio:</strong></p>" +
                "<blockquote style=\"background-color: #f9f9f9; padding: 15px; border-left: 5px solid #1D68A7;\">" +
                "<p style=\"margin: 0; font-style: italic;\">" + contactRequest.getMessage() + "</p>" +
                "</blockquote>" +
                "<p style=\"font-size: 14px; color: #555;\">Ti invitiamo a rispondere al più presto. Grazie per il tuo impegno!</p>" +
                "</body>" +
                "</html>";

        helper.setText(body, true); // Corpo HTML
        javaMailSender.send(message); // Invio email al gestore
    }

    // Metodo per inviare una email di conferma all'utente
    private void sendConfirmationToUser(ContactRequest contactRequest) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true per abilitare HTML

        helper.setFrom(fromEmail);  // Email del gestore
        helper.setTo(contactRequest.getEmail());  // Email dell'utente

        helper.setSubject("Conferma ricezione richiesta di contatto");

        // Corpo dell'email in HTML
        String body = "<html>" +
                "<body style=\"font-family: Arial, sans-serif;\">" +
                "<h2 style=\"color: #1D68A7;\">Grazie per averci contattato!</h2>" +
                "<p style=\"font-size: 16px; color: #333;\">Ciao " + contactRequest.getName() + ",<br>Grazie per averci scritto! Abbiamo ricevuto la tua richiesta di informazioni e uno dei nostri esperti ti risponderà a breve.</p>" +
                "<p style=\"color: #333;\">Ecco i dettagli che ci hai inviato:</p>" +
                "<p><strong style=\"color: #2A2A2A;\">Nome:</strong> " + contactRequest.getName() + "</p>" +
                "<p><strong style=\"color: #2A2A2A;\">Email:</strong> " + contactRequest.getEmail() + "</p>" +
                "<p><strong style=\"color: #2A2A2A;\">Messaggio:</strong></p>" +
                "<blockquote style=\"background-color: #f9f9f9; padding: 15px; border-left: 5px solid #1D68A7;\">" +
                "<p style=\"margin: 0; font-style: italic;\">" + contactRequest.getMessage() + "</p>" +
                "</blockquote>" +
                "<p style=\"font-size: 14px; color: #555;\">Ti risponderemo al più presto. Nel frattempo, se hai altre domande, non esitare a contattarci. A presto!</p>" +
                "<p style=\"font-size: 14px; color: #555;\">Il team di <strong>AdoptEasy</strong></p>" +
                "</body>" +
                "</html>";

        helper.setText(body, true); // Corpo HTML
        javaMailSender.send(message); // Invio email di conferma all'utente
    }
}
