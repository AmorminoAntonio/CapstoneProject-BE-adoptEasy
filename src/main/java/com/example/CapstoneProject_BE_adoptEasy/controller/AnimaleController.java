package com.example.CapstoneProject_BE_adoptEasy.controller;

import com.example.CapstoneProject_BE_adoptEasy.model.Animale;
import com.example.CapstoneProject_BE_adoptEasy.payload.AnimaleDTO;
import com.example.CapstoneProject_BE_adoptEasy.service.AnimaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AnimaleController {

    @Autowired
    AnimaleService animaleService;

    @PostMapping("/admin/animal/signup")
    public ResponseEntity<Animale> registerAnimal(@RequestBody AnimaleDTO animaleDTO) {
        Animale animal = animaleService.registerAnimal(animaleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(animal);
    }

    @GetMapping("/admin/animal/all")
    public List<Animale> getAllAnimals() {
        return animaleService.getAllAnimals();
    }

    @GetMapping("/admin/animal/{id}")
    public Animale adminFindAnimalById(@PathVariable Long id) {
        return animaleService.getAnimalById(id);
    }

    @GetMapping("/volunteer/animal/{id}")
    public Animale volunteerFindAnimalById(@PathVariable Long id) {
        return animaleService.getAnimalById(id);
    }

    @PutMapping("/admin/update/{id}")
    public Animale updateAnimal(@PathVariable Long id, @RequestBody AnimaleDTO animaleDTO) {
        return animaleService.updateAnimal(id, animaleDTO);
    }

    @DeleteMapping("/admin/delete/{id}")
    public void deleteAnimal(@PathVariable Long id) {
        animaleService.deleteAnimal(id);
    }
}