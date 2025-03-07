package com.example.CapstoneProject_BE_adoptEasy.controller;

import com.example.CapstoneProject_BE_adoptEasy.model.Adozione;
import com.example.CapstoneProject_BE_adoptEasy.payload.AdozioneDTO;
import com.example.CapstoneProject_BE_adoptEasy.service.AdozioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adozioni")
public class AdozioneController {
    @Autowired
    private AdozioneService adoptionService;

    // Endpoint per registrare una nuova adozione
    @PostMapping
    public ResponseEntity<Adozione> registerAdoption(@Validated @RequestBody AdozioneDTO adoptionDTO) {
        Adozione createdAdoption = adoptionService.registerAdozione(adoptionDTO);
        return new ResponseEntity<>(createdAdoption, HttpStatus.CREATED);
    }

    // Endpoint per ottenere tutte le adozioni
    @GetMapping
    public List<Adozione> getAllAdoptions() {
        return adoptionService.getAllAdozioni();
    }

    // Endpoint per ottenere un'adozione tramite ID
    @GetMapping("/{id}")
    public ResponseEntity<Adozione> getAdoptionById(@PathVariable Long id) {
        Adozione adoption = adoptionService.getAdozioneById(id);
        return new ResponseEntity<>(adoption, HttpStatus.OK);
    }

    // Endpoint per aggiornare un'adozione
    @PutMapping("/{id}")
    public ResponseEntity<Adozione> updateAdoption(@PathVariable Long id, @Validated @RequestBody AdozioneDTO adoptionDTO) {
        Adozione updatedAdoption = adoptionService.updateAdozione(id, adoptionDTO);
        return new ResponseEntity<>(updatedAdoption, HttpStatus.OK);
    }

    // Endpoint per eliminare un'adozione
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdozione(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
