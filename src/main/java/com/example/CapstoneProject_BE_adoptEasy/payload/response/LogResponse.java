package com.example.CapstoneProject_BE_adoptEasy.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {
    private String username;
    private String token;
}
