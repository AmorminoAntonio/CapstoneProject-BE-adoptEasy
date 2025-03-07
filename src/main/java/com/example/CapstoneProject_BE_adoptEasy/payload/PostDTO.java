package com.example.CapstoneProject_BE_adoptEasy.payload;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.PostStatusType;
import com.example.CapstoneProject_BE_adoptEasy.model.Animale;
import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PostDTO {

    private LocalDate publishDate;

    @NotBlank(message = "questo campo è obbligatorio")
    private String title;

    @NotBlank(message = "questo campo è obbligatorio")
    private String content;

    @NotBlank(message = "questo campo è obbligatorio")
    private String image;

    @NotBlank(message = "questo campo è obbligatorio")
    private PostStatusType status;

    private LocalDate lastModified;

    @NotBlank(message = "questo campo è obbligatorio")
    private Boolean visible;

    @NotBlank(message = "questo campo è obbligatorio")
    private Utente utente;

    @NotBlank(message = "questo campo è obbligatorio")
    private Animale animale;
}
