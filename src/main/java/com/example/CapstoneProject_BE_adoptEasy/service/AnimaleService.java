package com.example.CapstoneProject_BE_adoptEasy.service;

import com.example.CapstoneProject_BE_adoptEasy.exception.AnimaleFoundException;
import com.example.CapstoneProject_BE_adoptEasy.model.Animale;
import com.example.CapstoneProject_BE_adoptEasy.payload.AnimaleDTO;
import com.example.CapstoneProject_BE_adoptEasy.repository.AnimaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnimaleService {
    @Autowired
    AnimaleRepository animaleRepository;

    // Metodo per registrare un nuovo animale, accessibile solo da ADMIN o VOLUNTEER
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Animale registerAnimal(AnimaleDTO registrationReq) {
        // Convertiamo il DTO in Entity
        Animale animal = toEntity(registrationReq);
        // Salviamo l'entità nel database
        return animaleRepository.save(animal);
    }

    // Metodo per ottenere tutti gli animali
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Page<AnimaleDTO> getAllAnimals(Pageable pageable) {
        Page<Animale> animals = animaleRepository.findAll(pageable);

        if (animals.isEmpty()) {
            throw new RuntimeException("Siamo spiacenti. Nessuna adozione trovata.");
        }

        List<AnimaleDTO> adoptionDTOList = animals.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(adoptionDTOList, pageable, animals.getTotalElements());
    }

    // Metodo per ottenere un animale tramite ID
    public AnimaleDTO getAnimalById(Long id) {
        Animale animale = animaleRepository.findById(id)
                .orElseThrow(() -> new AnimaleFoundException(id));  // Se non trovato, lancia eccezione con ID specifico

        return toDto(animale);
    }

    // Metodo per aggiornare un animale, accessibile solo da ADMIN o VOLUNTEER
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Animale updateAnimal(Long id, AnimaleDTO registrationDto) {
        Animale existingAnimal = animaleRepository.findById(id)
                .orElseThrow(() -> new AnimaleFoundException(id));  // Se non trovato, lancia eccezione con ID specifico

        // Aggiorniamo i dettagli dell'animale
        existingAnimal.setSpecies(registrationDto.getSpecies());
        existingAnimal.setBreed(registrationDto.getBreed());
        existingAnimal.setFoundDate(registrationDto.getFoundDate());
        existingAnimal.setDescription(registrationDto.getDescription());
        existingAnimal.setStatus(registrationDto.getStatus());
        existingAnimal.setAvailableSince(registrationDto.getAvailableSince());
        existingAnimal.setPhoto(registrationDto.getPhoto());
        existingAnimal.setFoundLocation(registrationDto.getFoundLocation());
        existingAnimal.setObservation(registrationDto.getObservation());

        // Salviamo l'animale aggiornato
        return animaleRepository.save(existingAnimal);
    }

    // Metodo per eliminare un animale, accessibile solo da ADMIN e VOLUNTEER
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public void deleteAnimal(Long id) {
        Animale animal = animaleRepository.findById(id)
                .orElseThrow(() -> new AnimaleFoundException(id));  // Se non trovato, lancia eccezione con ID specifico
        animaleRepository.delete(animal);
    }

    // Metodo di conversione da DTO a Entity
    public Animale toEntity(AnimaleDTO registrationDto) {
        Animale animal = new Animale();
        animal.setSpecies(registrationDto.getSpecies());
        animal.setBreed(registrationDto.getBreed());
        animal.setFoundDate(registrationDto.getFoundDate());
        animal.setDescription(registrationDto.getDescription());
        animal.setStatus(registrationDto.getStatus());
        animal.setAvailableSince(registrationDto.getAvailableSince());
        animal.setPhoto(registrationDto.getPhoto());
        animal.setFoundLocation(registrationDto.getFoundLocation());
        animal.setObservation(registrationDto.getObservation());
        return animal;
    }

    // Metodo di conversione da Entity a DTO
    public AnimaleDTO toDto(Animale animal) {
        AnimaleDTO registrationDto = new AnimaleDTO();
        registrationDto.setSpecies(animal.getSpecies());
        registrationDto.setBreed(animal.getBreed());
        registrationDto.setFoundDate(animal.getFoundDate());
        registrationDto.setDescription(animal.getDescription());
        registrationDto.setStatus(animal.getStatus());
        registrationDto.setAvailableSince(animal.getAvailableSince());
        registrationDto.setPhoto(animal.getPhoto());
        registrationDto.setFoundLocation(animal.getFoundLocation());
        registrationDto.setObservation(animal.getObservation());
        return registrationDto;
    }

}
