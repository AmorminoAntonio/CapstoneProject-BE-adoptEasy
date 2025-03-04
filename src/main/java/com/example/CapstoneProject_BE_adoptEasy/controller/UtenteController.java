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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/utente")
public class UtenteController {
    @Autowired
    UtenteService utenteService;

    @Autowired
    Cloudinary cloudinaryConfig;


    @PostMapping("/registration")
    public ResponseEntity<String> signUp(@Validated @RequestBody RegistrationReq nuovoUtente, BindingResult validazione) {

        if (validazione.hasErrors()) {
            StringBuilder errori = new StringBuilder("Problemi nella validazione dati :\n");

            for (ObjectError errore : validazione.getAllErrors()) {
                errori.append(errore.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(errori.toString(), HttpStatus.BAD_REQUEST);
        }

        try {

            String messaggio = String.valueOf(utenteService.registrazioneUtente(nuovoUtente));
            return new ResponseEntity<>(messaggio, HttpStatus.OK);
        } catch (UsernameDuplicatedException | EmailDuplicatedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
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


    @PatchMapping("/auth/avatar/{idUtente}")
    public ResponseEntity<?> cambiaAvatarUtente(@RequestPart("avatar") MultipartFile avatar, @PathVariable long idUtente) {
        try {
            Map mappa = cloudinaryConfig.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
            String urlImage = mappa.get("secure_url").toString();
            utenteService.updateAvatar(idUtente, urlImage);
            return new ResponseEntity<>("Immagine avatar sostituita", HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException("Errore nel caricamento dell'immagine: " + e);
        }
    }

}
