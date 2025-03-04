package com.example.CapstoneProject_BE_adoptEasy.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginReq {
    @NotBlank(message = "Username è un campo obbligatorio")
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank(message = "Password è un campo obbligatorio")
    @Size(min = 3, max = 20)
    private String password;
}
