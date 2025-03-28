package com.example.CapstoneProject_BE_adoptEasy.service;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.AdoptionStatusType;
import com.example.CapstoneProject_BE_adoptEasy.model.Adozione;
import com.example.CapstoneProject_BE_adoptEasy.model.Animale;
import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import com.example.CapstoneProject_BE_adoptEasy.payload.AdozioneDTO;
import com.example.CapstoneProject_BE_adoptEasy.repository.AdozioneRepository;
import com.example.CapstoneProject_BE_adoptEasy.repository.AnimaleRepository;
import com.example.CapstoneProject_BE_adoptEasy.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdozioneService {

    @Autowired
    private AdozioneRepository adozioneRepository;

    @Autowired
    private AnimaleRepository animaleRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Adozione registerAdozione(AdozioneDTO adoptionDTO) {
        Adozione adoption = toEntity(adoptionDTO);
        return adozioneRepository.save(adoption);
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Page<AdozioneDTO> getAllAdozioni(Pageable pageable) {
        Page<Adozione> adoptions = adozioneRepository.findAll(pageable);

        if (adoptions.isEmpty()) {
            throw new RuntimeException("Siamo spiacenti. Nessuna adozione trovata.");
        }

        List<AdozioneDTO> adoptionDTOList = adoptions.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(adoptionDTOList, pageable, adoptions.getTotalElements());
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Adozione updateAdozione(Long id, AdozioneDTO adoptionDTO) {
        // Recupera l'adozione esistente
        Adozione existingAdozione = adozioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adozione con ID " + id + " non trovata."));

        // Controlla se l'adozione è già completata o rifiutata
        if (AdoptionStatusType.COMPLETED.equals(existingAdozione.getStatus())) {
            throw new RuntimeException("L'adozione è già completata e non può essere modificata.");
        }
        if (AdoptionStatusType.REJECTED.equals(existingAdozione.getStatus())) {
            throw new RuntimeException("Ci dispiace, l'adozione è stata rifiutata.");
        }

        // Imposta le nuove note e verifica se i documenti sono verificati
        existingAdozione.setAdoptionNotes(adoptionDTO.getAdoptionNotes());
        existingAdozione.setStatus(adoptionDTO.getStatus());
        existingAdozione.setDocumentsVerified(adoptionDTO.getDocumentsVerified());

        // Gestione dello stato APPROVED
        if (adoptionDTO.getDocumentsVerified() && AdoptionStatusType.PENDING.equals(existingAdozione.getStatus())) {
            existingAdozione.setStatus(AdoptionStatusType.APPROVED);
        }

        // Gestione dello stato COMPLETED
        if (AdoptionStatusType.APPROVED.equals(existingAdozione.getStatus()) && existingAdozione.getStartDate() != null) {
            existingAdozione.setStatus(AdoptionStatusType.COMPLETED);
            existingAdozione.setEndDate(LocalDate.now());
        }

        // Gestione dello stato REJECTED
        if (AdoptionStatusType.REJECTED.equals(adoptionDTO.getStatus())) {
            existingAdozione.setStatus(AdoptionStatusType.REJECTED);
            existingAdozione.setEndDate(LocalDate.now());
        }

        return adozioneRepository.save(existingAdozione);
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public AdozioneDTO getAdozioneById(Long id) {
        Adozione adozione = adozioneRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("elemento non trovato con id: " + id));  // Se non trovato, lancia eccezione con ID specifico

        return toDto(adozione);
    }

    // Metodo per eliminare un'adozione
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAdozione(Long id) {
        Adozione adoption = adozioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adozione con ID " + id + " non trovata."));
        adozioneRepository.delete(adoption);
    }

    // Metodo di conversione da DTO a Entity
    public Adozione toEntity(AdozioneDTO adoptionDTO) {
        Adozione adoption = new Adozione();
        adoption.setStartDate(LocalDate.now());
        adoption.setEndDate(null);
        adoption.setAdoptionNotes(adoptionDTO.getAdoptionNotes());
        adoption.setStatus(AdoptionStatusType.PENDING);  // Stato iniziale PENDING
        adoption.setDocumentsVerified(false);  // Imposta i documenti come non verificati

        // Recupera Animale e Utente tramite gli ID
        Animale animale = animaleRepository.findById(adoptionDTO.getAnimaleId().getId_animal())
                .orElseThrow(() -> new RuntimeException("Animale non trovato con ID " + adoptionDTO.getAnimaleId()));

        Utente utente = utenteRepository.findById(adoptionDTO.getUtenteId().getId_user())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID " + adoptionDTO.getUtenteId()));

        adoption.setAnimale(animale);
        adoption.setUtente(utente);

        return adoption;
    }


    // Metodo di conversione da Entity a DTO
    public AdozioneDTO toDto(Adozione adoption) {
        AdozioneDTO adoptionDTO = new AdozioneDTO();
        adoptionDTO.setId(adoption.getId_adoption());
        adoptionDTO.setStartDate(adoption.getStartDate());
        adoptionDTO.setEndDate(adoption.getEndDate());
        adoptionDTO.setAdoptionNotes(adoption.getAdoptionNotes());
        adoptionDTO.setStatus(adoption.getStatus());
        adoptionDTO.setDocumentsVerified(adoption.getDocumentsVerified());

        // Imposta gli ID di Animale e Utente, non gli oggetti completi
        if (adoption.getAnimale() != null) {
            adoptionDTO.setAnimaleId(adoption.getAnimale());
        }

        if (adoption.getUtente() != null) {
            adoptionDTO.setUtenteId(adoption.getUtente());
        }

        return adoptionDTO;
    }
}
