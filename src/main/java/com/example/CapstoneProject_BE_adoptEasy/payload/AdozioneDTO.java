package com.example.CapstoneProject_BE_adoptEasy.payload;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.AdoptionStatusType;
import com.example.CapstoneProject_BE_adoptEasy.model.Animale;
import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdozioneDTO {

    @NotNull(message = "questo campo è obbligatorio")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank(message = "questo campo è obbligatorio")
    private String adoptionNotes;

    private AdoptionStatusType status;

    private Boolean documentsVerified;

    @NotNull(message = "questo campo è obbligatorio")
    private long animaleId;

    @NotNull(message = "questo campo è obbligatorio")
    private long utenteId;
}
