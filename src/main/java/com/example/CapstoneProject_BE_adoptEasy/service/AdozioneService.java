package com.example.CapstoneProject_BE_adoptEasy.service;

import com.example.CapstoneProject_BE_adoptEasy.model.Adozione;
import com.example.CapstoneProject_BE_adoptEasy.payload.AdozioneDTO;
import com.example.CapstoneProject_BE_adoptEasy.repository.AdozioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AdozioneService {

    @Autowired
    private AdozioneRepository adoptionRepository;

    // Metodo per registrare una nuova adozione, accessibile solo da ADMIN o VOLUNTEER
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Adozione registerAdozione(AdozioneDTO adoptionDTO) {
        // Convertiamo il DTO in Entity
        Adozione adoption = toEntity(adoptionDTO);
        // Salviamo l'entità nel database
        return adoptionRepository.save(adoption);
    }

    // Metodo per ottenere tutte le adozioni
    public List<Adozione> getAllAdozioni() {
        List<Adozione> adoptions = adoptionRepository.findAll();
        if (adoptions.isEmpty()) {
            throw new RuntimeException("Nessuna adozione trovata.");
        }
        return adoptions;
    }

    // Metodo per ottenere un'adozione tramite ID
    public Adozione getAdozioneById(Long id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adozione con ID " + id + " non trovata."));
    }

    // Metodo per aggiornare un'adozione
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Adozione updateAdozione(Long id, AdozioneDTO adoptionDTO) {
        Adozione existingAdozione = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adozione con ID " + id + " non trovata."));

        // Aggiorniamo i dettagli dell'adozione
        existingAdozione.setStartDate(adoptionDTO.getStartDate());
        existingAdozione.setEndDate(adoptionDTO.getEndDate());
        existingAdozione.setAdoptionNotes(adoptionDTO.getAdoptionNotes());
        existingAdozione.setStatus(adoptionDTO.getStatus());
        existingAdozione.setDocumentsVerified(adoptionDTO.getDocumentsVerified());
        existingAdozione.setAnimale(adoptionDTO.getAnimale());
        existingAdozione.setUtente(adoptionDTO.getUtente());

        // Salviamo l'adozione aggiornata
        return adoptionRepository.save(existingAdozione);
    }

    // Metodo per eliminare un'adozione
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAdozione(Long id) {
        Adozione adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adozione con ID " + id + " non trovata."));
        adoptionRepository.delete(adoption);
    }

    // Metodo di conversione da DTO a Entity
    public Adozione toEntity(AdozioneDTO adoptionDTO) {
        Adozione adoption = new Adozione();
        adoption.setStartDate(adoptionDTO.getStartDate());
        adoption.setEndDate(adoptionDTO.getEndDate());
        adoption.setAdoptionNotes(adoptionDTO.getAdoptionNotes());
        adoption.setStatus(adoptionDTO.getStatus());
        adoption.setDocumentsVerified(adoptionDTO.getDocumentsVerified());
        adoption.setAnimale(adoptionDTO.getAnimale());
        adoption.setUtente(adoptionDTO.getUtente());
        return adoption;
    }

    // Metodo di conversione da Entity a DTO
    public AdozioneDTO toDto(Adozione adoption) {
        AdozioneDTO adoptionDTO = new AdozioneDTO();
        adoptionDTO.setStartDate(adoption.getStartDate());
        adoptionDTO.setEndDate(adoption.getEndDate());
        adoptionDTO.setAdoptionNotes(adoption.getAdoptionNotes());
        adoptionDTO.setStatus(adoption.getStatus());
        adoptionDTO.setDocumentsVerified(adoption.getDocumentsVerified());
        adoptionDTO.setAnimale(adoption.getAnimale());
        adoptionDTO.setUtente(adoption.getUtente());
        return adoptionDTO;
    }
}
