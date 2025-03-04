package com.example.CapstoneProject_BE_adoptEasy.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
    // Info che ritorneremo all'interno di un oggetto JSON tramite ResponseEntity
    private String username;
    private Long id;
    private String email;
    private String role;
    private String token;
    private String type = "Bearer ";

    public JwtResponse(String username, Long id, String email, String role, String token) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.role = role;
        this.token = token;
    }
}
