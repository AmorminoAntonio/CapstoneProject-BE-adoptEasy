package com.example.CapstoneProject_BE_adoptEasy.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.CapstoneProject_BE_adoptEasy.exception.EmailDuplicatedException;
import com.example.CapstoneProject_BE_adoptEasy.exception.UsernameDuplicatedException;
import com.example.CapstoneProject_BE_adoptEasy.payload.request.LoginReq;
import com.example.CapstoneProject_BE_adoptEasy.payload.request.RegistrationReq;
import com.example.CapstoneProject_BE_adoptEasy.payload.response.LogResponse;
import com.example.CapstoneProject_BE_adoptEasy.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/utente")
public class UtenteController {
    @Autowired
    UtenteService utenteService;

    @Autowired
    Cloudinary cloudinaryConfig;


    @PostMapping("/registration") // libero a TUTTI
    public ResponseEntity<?> signUp(@Validated @RequestBody RegistrationReq nuovoUtente, BindingResult validazione) {
        if (validazione.hasErrors()) {
            // Crea una mappa per i messaggi di errore
            Map<String, String> errori = new HashMap<>();

            for (ObjectError errore : validazione.getAllErrors()) {
                errori.put("errore", errore.getDefaultMessage()); // Puoi anche usare un'altra struttura a seconda delle tue necessità
            }

            // Restituisci la mappa come risposta JSON con errore 400
            return new ResponseEntity<>(errori, HttpStatus.BAD_REQUEST);
        }

        try {
            String messaggio = String.valueOf(utenteService.registrazioneUtente(nuovoUtente));

            // Restituisci messaggio in formato JSON
            Map<String, String> successMessage = new HashMap<>();
            successMessage.put("message", messaggio);

            return new ResponseEntity<>(successMessage, HttpStatus.OK);
        } catch (UsernameDuplicatedException | EmailDuplicatedException e) {
            // Restituisci l'errore come oggetto JSON
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<String> modificaUtenteAdminVolunteer(@PathVariable long id, @RequestBody RegistrationReq updatedUserInfo) {
        try {
            // Invoca il servizio per aggiornare l'utente
            String responseMessage = utenteService.modificaUtenteAdminVolunteer(id, updatedUserInfo);
            return ResponseEntity.ok(responseMessage);
        } catch (RuntimeException e) {
            // Se c'è un errore (utente non trovato), restituisci un errore
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity<Page<RegistrationReq>> getAllUtenti(Pageable pageable) {
        Page<RegistrationReq> response = utenteService.getAllUtenti(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login") // libero a TUTTI
    public ResponseEntity<?> login(@Validated @RequestBody LoginReq loginReq, BindingResult checkValidazione) {

        try {

            if (checkValidazione.hasErrors()) {
                StringBuilder erroriValidazione = new StringBuilder("Problemi nella validazione\n");
                for (ObjectError errore : checkValidazione.getAllErrors()) {
                    erroriValidazione.append(errore.getDefaultMessage());
                }

                return new ResponseEntity<>(erroriValidazione.toString(), HttpStatus.BAD_REQUEST);
            }

            LogResponse response = utenteService.login(loginReq.getUsername(), loginReq.getPassword());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Credenziali non valide" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/adopter/avatar/{idUtente}") // modifica dell' avatar (permesso a livello di user ADOPTER)
    public ResponseEntity<?> cambiaAvatarUtente(@RequestPart("avatar") MultipartFile avatar, @PathVariable long idUtente) {
        try {
            Map mappa = cloudinaryConfig.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
            String urlImage = mappa.get("secure_url").toString();
            utenteService.modificaAvatar(idUtente, urlImage);
            return new ResponseEntity<>("Immagine avatar sostituita", HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento dell'immagine: " + e);
        }
    }

    @DeleteMapping("/admin/delete/{id}") // solo ADMIN
    public ResponseEntity<String> deleteUtente(@PathVariable Long id) {
        String response = utenteService.deleteUtente(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

