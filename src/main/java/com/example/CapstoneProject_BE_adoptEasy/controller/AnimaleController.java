package com.example.CapstoneProject_BE_adoptEasy.controller;

import com.example.CapstoneProject_BE_adoptEasy.exception.AnimaleFoundException;
import com.example.CapstoneProject_BE_adoptEasy.model.Animale;
import com.example.CapstoneProject_BE_adoptEasy.payload.AnimaleDTO;
import com.example.CapstoneProject_BE_adoptEasy.service.AnimaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AnimaleController {

    @Autowired
    AnimaleService animaleService;

    @PostMapping("/admin/animal/signup")
    public ResponseEntity<Animale> admRegisterAnimal(@RequestBody AnimaleDTO animaleDTO) {
        Animale animal = animaleService.registerAnimal(animaleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(animal);
    }

    @PostMapping("/volunteer/animal/signup")
    public ResponseEntity<Animale> volRegisterAnimal(@RequestBody AnimaleDTO animaleDTO) {
        Animale animal = animaleService.registerAnimal(animaleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(animal);
    }

    @GetMapping("/admin/animal/all")
    public ResponseEntity<Page<AnimaleDTO>> admGetAllAnimals(Pageable pageable) {
        try {
            Page<AnimaleDTO> animals = animaleService.getAllAnimals(pageable);
            return new ResponseEntity<>(animals, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/volunteer/animal/all")
    public ResponseEntity<Page<AnimaleDTO>> volGetAllAnimals(Pageable pageable) {
        try {
            Page<AnimaleDTO> animals = animaleService.getAllAnimals(pageable);
            return new ResponseEntity<>(animals, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/adopter/animal/all")
    public ResponseEntity<Page<AnimaleDTO>> getAllAnimals(Pageable pageable) {
        try {
            Page<AnimaleDTO> animals = animaleService.getAllAnimalsFree(pageable);
            return new ResponseEntity<>(animals, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/animal/{id}")
    public ResponseEntity<AnimaleDTO> admGetAnimalById(@PathVariable Long id) {
        try {
            AnimaleDTO animaleDTO = animaleService.getAnimalById(id);
            return new ResponseEntity<>(animaleDTO, HttpStatus.OK);  // Restituisce il DTO con OK
        } catch (AnimaleFoundException e) {
            // Se l'animale non viene trovato, restituisce un errore con messaggio
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/volunteer/animal/{id}")
    public ResponseEntity<AnimaleDTO> volGetAnimalById(@PathVariable Long id) {
        try {
            AnimaleDTO animaleDTO = animaleService.getAnimalById(id);
            return new ResponseEntity<>(animaleDTO, HttpStatus.OK);  // Restituisce il DTO con OK
        } catch (AnimaleFoundException e) {
            // Se l'animale non viene trovato, restituisce un errore con messaggio
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/admin/animal/update/{id}")
    public ResponseEntity<Animale> updateAnimal(@PathVariable Long id, @RequestBody AnimaleDTO animaleDTO) {
        try {
            Animale updatedAnimal = animaleService.updateAnimal(id, animaleDTO);
            return new ResponseEntity<>(updatedAnimal, HttpStatus.OK); // Restituisce l'animale aggiornato con status 200 OK
        } catch (AnimaleFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Se l'animale non viene trovato, restituisce 404
        }
    }

    @PutMapping("/volunteer/animal/update/{id}")
    public ResponseEntity<Animale> VolUpdateAnimal(@PathVariable Long id, @RequestBody AnimaleDTO animaleDTO) {
        try {
            Animale updatedAnimal = animaleService.updateAnimal(id, animaleDTO);
            return new ResponseEntity<>(updatedAnimal, HttpStatus.OK); // Restituisce l'animale aggiornato con status 200 OK
        } catch (AnimaleFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Se l'animale non viene trovato, restituisce 404
        }
    }


    @DeleteMapping("/admin/animal/delete/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long id) {
        try {
            animaleService.deleteAnimal(id);
            return new ResponseEntity<>("Animale cancellato con successo", HttpStatus.NO_CONTENT); // Restituisce status 204 NO CONTENT
        } catch (AnimaleFoundException e) {
            return new ResponseEntity<>("Animale non trovato", HttpStatus.NOT_FOUND); // Se l'animale non viene trovato, restituisce 404
        }
    }

    @DeleteMapping("/volunteer/animal/delete/{id}")
    public ResponseEntity<?> VolDeleteAnimal(@PathVariable Long id) {
        try {
            animaleService.deleteAnimal(id);
            return new ResponseEntity<>("Animale cancellato con successo", HttpStatus.NO_CONTENT); // Restituisce status 204 NO CONTENT
        } catch (AnimaleFoundException e) {
            return new ResponseEntity<>("Animale non trovato", HttpStatus.NOT_FOUND); // Se l'animale non viene trovato, restituisce 404
        }
    }
}