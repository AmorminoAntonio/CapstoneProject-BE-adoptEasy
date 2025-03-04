package com.example.CapstoneProject_BE_adoptEasy.payload;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.AnimalStatusType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class AnimaleDTO {

    @NotBlank(message = "questo campo è obbligatorio")
    private String species;

    @NotBlank(message = "questo campo è obbligatorio")
    private String breed;

    @NotBlank(message = "questo campo è obbligatorio")
    private LocalDateTime foundDate;

    @NotBlank(message = "questo campo è obbligatorio")
    private String description;

    @NotBlank(message = "questo campo è obbligatorio")
    private AnimalStatusType status;

    @NotBlank(message = "questo campo è obbligatorio")
    private LocalDateTime availableSince;

    @NotBlank(message = "questo campo è obbligatorio")
    @URL(protocol = "https")
    private String photo;

    @NotBlank(message = "questo campo è obbligatorio")
    private String foundLocation;

    @NotBlank(message = "questo campo è obbligatorio")
    private String observation;
}
