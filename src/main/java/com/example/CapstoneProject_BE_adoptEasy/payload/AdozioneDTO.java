package com.example.CapstoneProject_BE_adoptEasy.payload;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.AdoptionStatusType;
import com.example.CapstoneProject_BE_adoptEasy.model.Animale;
import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdozioneDTO {

    @NotBlank(message = "questo campo è obbligatorio")
    private LocalDateTime startDate;

    @NotBlank(message = "questo campo è obbligatorio")
    private LocalDateTime endDate;

    @NotBlank(message = "questo campo è obbligatorio")
    private String adoptionNotes;

    @NotBlank(message = "questo campo è obbligatorio")
    private AdoptionStatusType status;

    @NotBlank(message = "questo campo è obbligatorio")
    private Boolean documentsVerified;

    @NotBlank(message = "questo campo è obbligatorio")
    private Animale animale;

    @NotBlank(message = "questo campo è obbligatorio")
    private Utente utente;
}
