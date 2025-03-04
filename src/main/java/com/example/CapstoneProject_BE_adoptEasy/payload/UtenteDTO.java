package com.example.CapstoneProject_BE_adoptEasy.payload;

import com.example.CapstoneProject_BE_adoptEasy.enumerated.RoleType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class UtenteDTO {
    @NotBlank(message = "questo campo è obbligatorio")
    private String firstName;

    @NotBlank(message = "questo campo è obbligatorio")
    private String lastName;

    @NotBlank(message = "questo campo è obbligatorio")
    private String email;

    @NotBlank(message = "questo campo è obbligatorio")
    private String password;

    @NotBlank(message = "questo campo è obbligatorio")
    private LocalDateTime registrationDate;

    @NotBlank(message = "questo campo è obbligatorio")
    private String phone;

    @NotBlank(message = "questo campo è obbligatorio")
    private String address;

    @NotBlank(message = "questo campo è obbligatorio")
    private RoleType role;

    @URL(protocol = "https")
    private String avatarUtente;
}
