package com.example.CapstoneProject_BE_adoptEasy.controller;

import com.example.CapstoneProject_BE_adoptEasy.payload.ContactRequest;
import com.example.CapstoneProject_BE_adoptEasy.payload.response.ResponseMessage;
import com.example.CapstoneProject_BE_adoptEasy.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class ContactUsController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/contact")
    public ResponseEntity<?> sendContactMessage(@RequestBody ContactRequest contactRequest) {
        try {
            emailService.sendContactEmail(contactRequest);  // Chiamata al servizio per inviare le email
            // Risposta JSON in caso di successo
            return ResponseEntity.ok(new ResponseMessage("success", "Richiesta di contatto inviata con successo."));
        } catch (MessagingException e) {
            // Risposta JSON in caso di errore
            return ResponseEntity.status(500).body(new ResponseMessage("error", "Errore nell'invio della richiesta di contatto."));
        }
    }
}
