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
import java.util.stream.Collectors;

@Service
@Transactional
public class AdozioneService {

    @Autowired
    private AdozioneRepository adoptionRepository;

    @Autowired
    private AnimaleRepository animaleRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Adozione registerAdozione(AdozioneDTO adoptionDTO) {
        Adozione adoption = toEntity(adoptionDTO);
        return adoptionRepository.save(adoption);
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public Page<AdozioneDTO> getAllAdozioni(Pageable pageable) {
        Page<Adozione> adoptions = adoptionRepository.findAll(pageable);

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
        Adozione existingAdozione = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adozione con ID " + id + " non trovata."));

        if (AdoptionStatusType.COMPLETED.equals(existingAdozione.getStatus())) {
            throw new RuntimeException("L'adozione è già completata e non può essere modificata.");
        }

        existingAdozione.setAdoptionNotes(adoptionDTO.getAdoptionNotes());
        existingAdozione.setStatus(adoptionDTO.getStatus());
        existingAdozione.setDocumentsVerified(adoptionDTO.getDocumentsVerified());

        if (adoptionDTO.getDocumentsVerified() && AdoptionStatusType.PENDING.equals(existingAdozione.getStatus())) {
            existingAdozione.setStatus(AdoptionStatusType.APPROVED);
        }

        if (AdoptionStatusType.APPROVED.equals(existingAdozione.getStatus()) && existingAdozione.getStartDate() != null) {
            existingAdozione.setStatus(AdoptionStatusType.COMPLETED);
            existingAdozione.setEndDate(LocalDate.now());
        }

        return adoptionRepository.save(existingAdozione);
    }


    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VOLUNTEER')")
    public AdozioneDTO getAdozioneById(Long id) {
        Adozione adozione = adoptionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("elemento non trovato con id: " + id));  // Se non trovato, lancia eccezione con ID specifico

        return toDto(adozione);
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
        adoption.setStartDate(LocalDate.now());
        adoption.setEndDate(null);
        adoption.setAdoptionNotes(adoptionDTO.getAdoptionNotes());
        adoption.setStatus(AdoptionStatusType.PENDING);
        adoption.setDocumentsVerified(false);

        Animale animale = animaleRepository.findById(adoptionDTO.getAnimaleId())
                .orElseThrow(() -> new RuntimeException("Animale non trovato con ID " + adoptionDTO.getAnimaleId()));

        Utente utente = utenteRepository.findById(adoptionDTO.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID " + adoptionDTO.getUtenteId()));

        adoption.setAnimale(animale);
        adoption.setUtente(utente);
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
        adoptionDTO.setAnimaleId(adoption.getAnimale().getId_animal());
        adoptionDTO.setUtenteId(adoption.getUtente().getId_user());
        return adoptionDTO;
    }
}
