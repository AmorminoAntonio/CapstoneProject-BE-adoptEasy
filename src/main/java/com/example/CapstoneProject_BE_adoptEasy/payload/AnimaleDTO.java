package com.example.CapstoneProject_BE_adoptEasy.payload;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.AnimalStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data
public class AnimaleDTO {
    private long id;

    @NotBlank(message = "questo campo è obbligatorio")
    private String species;

    @NotBlank(message = "questo campo è obbligatorio")
    private String breed;

    @NotNull(message = "questo campo è obbligatorio")
    private LocalDate foundDate;

    @NotBlank(message = "questo campo è obbligatorio")
    private String description;

    @NotBlank(message = "questo campo è obbligatorio")
    private AnimalStatusType status;

    @NotNull(message = "questo campo è obbligatorio")
    private LocalDate availableSince;

    @NotNull(message = "questo campo è obbligatorio")
    @URL(protocol = "https")
    private String photo;

    @NotBlank(message = "questo campo è obbligatorio")
    private String foundLocation;

    @NotBlank(message = "questo campo è obbligatorio")
    private String observation;

}
