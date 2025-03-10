package com.example.CapstoneProject_BE_adoptEasy.payload;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.PostStatusType;
import com.example.CapstoneProject_BE_adoptEasy.model.Animale;
import com.example.CapstoneProject_BE_adoptEasy.model.Utente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import java.time.LocalDate;


@Data
public class PostDTO {

    private LocalDate publishDate;

    @NotBlank(message = "questo campo è obbligatorio")
    private String title;

    @NotBlank(message = "questo campo è obbligatorio")
    private String content;

    @NotBlank(message = "questo campo è obbligatorio")
    @URL(protocol = "https")
    private String image;

    private long utenteID;

    private long animaleID;

}
