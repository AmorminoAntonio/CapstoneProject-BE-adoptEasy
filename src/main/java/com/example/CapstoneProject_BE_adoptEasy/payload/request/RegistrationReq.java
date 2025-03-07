package com.example.CapstoneProject_BE_adoptEasy.payload.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

@Data
public class RegistrationReq {

    @NotBlank(message = "Username è un campo obbligatorio")
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank(message = "fName: questo campo è obbligatorio")
    private String firstName;

    @NotBlank(message = "lName: questo campo è obbligatorio")
    private String lastName;

    @NotBlank(message = "email: questo campo è obbligatorio")
    @Email(message = "Il formato email inserito non è valido")
    private String email;

    @NotBlank(message = "pk: questo campo è obbligatorio")
    @Size(min = 3, max = 20)
    private String password;

    private LocalDate registrationDate;

    @NotBlank(message = "phone: questo campo è obbligatorio")
    private String phone;

    @NotBlank(message = "address: questo campo è obbligatorio")
    private String address;

    private String role;

    @URL(protocol = "https")
    private String avatarUtente;
}
