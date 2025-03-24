package com.example.CapstoneProject_BE_adoptEasy.payload;

import lombok.Data;

@Data
public class ContactRequest {
    private String name;
    private String email;
    private String message;
}
