package com.example.CapstoneProject_BE_adoptEasy.controller;

import com.example.CapstoneProject_BE_adoptEasy.exception.AnimaleFoundException;
import com.example.CapstoneProject_BE_adoptEasy.model.Adozione;
import com.example.CapstoneProject_BE_adoptEasy.payload.AdozioneDTO;
import com.example.CapstoneProject_BE_adoptEasy.service.AdozioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AdozioneController {
    @Autowired
    private AdozioneService adozioneService;

    // Metodo per registrare una nuova adozione
    @PostMapping("/volunteer/adozioni/register")
    public ResponseEntity<String> volRegisterAdozione(@RequestBody AdozioneDTO adoptionDTO) {
        try {
            Adozione adoption = adozioneService.registerAdozione(adoptionDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Adozione registrata con successo con stato iniziale: PENDING");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante la registrazione: " + e.getMessage());
        }
    }

    @PostMapping("/admin/adozioni/register")
    public ResponseEntity<String> admRegisterAdozione(@RequestBody AdozioneDTO adoptionDTO) {
        try {
            Adozione adoption = adozioneService.registerAdozione(adoptionDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Adozione registrata con successo con stato iniziale: PENDING");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante la registrazione: " + e.getMessage());
        }
    }

    // Metodo per ottenere tutte le adozioni
    @GetMapping("/admin/adozioni/all")
    public ResponseEntity<Page<AdozioneDTO>> admGetAllAdozioni(Pageable pageable) {
        try {
            Page<AdozioneDTO> adoptions = adozioneService.getAllAdozioni(pageable);
            return new ResponseEntity<>(adoptions, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/volunteer/adozioni/all")
    public ResponseEntity<Page<AdozioneDTO>> volGetAllAdozioni(Pageable pageable) {
        try {
            Page<AdozioneDTO> adoptions = adozioneService.getAllAdozioni(pageable);
            return new ResponseEntity<>(adoptions, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    // Metodo per ottenere un'adozione tramite ID
    @GetMapping("/volunteer/adozioni/{id}")
    public ResponseEntity<String> volGetAdozioneById(@PathVariable Long id) {
        try {
            AdozioneDTO adoption = adozioneService.getAdozioneById(id);
            return ResponseEntity.ok("Adozione trovata: " + adoption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adozione con ID " + id + " non trovata");
        }
    }

    @GetMapping("/admin/adozioni/{id}")
    public ResponseEntity<AdozioneDTO> admGetAdozioneById(@PathVariable Long id) {
        try {
            AdozioneDTO adozioneDTO = adozioneService.getAdozioneById(id);
            return new ResponseEntity<>(adozioneDTO, HttpStatus.OK);  // Restituisce il DTO con OK
        } catch (AnimaleFoundException e) {
            // Se l'animale non viene trovato, restituisce un errore con messaggio
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Metodo per aggiornare un'adozione
    @PutMapping("/admin/adozioni/update/{id}")
    public ResponseEntity<String> updateAdozione(@PathVariable Long id, @RequestBody AdozioneDTO adoptionDTO) {
        try {
            Adozione updatedAdozione = adozioneService.updateAdozione(id, adoptionDTO);
            return ResponseEntity.ok("Adozione aggiornata con successo, nuovo stato: " + updatedAdozione.getStatus());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore durante l'aggiornamento: " + e.getMessage());
        }
    }

    // Metodo per eliminare un'adozione
    @DeleteMapping("/admin/adozioni/delete/{id}")
    public ResponseEntity<String> deleteAdozione(@PathVariable Long id) {
        try {
            adozioneService.deleteAdozione(id);
            return ResponseEntity.ok("Adozione eliminata con successo");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore nell'eliminazione: " + e.getMessage());
        }
    }
}
