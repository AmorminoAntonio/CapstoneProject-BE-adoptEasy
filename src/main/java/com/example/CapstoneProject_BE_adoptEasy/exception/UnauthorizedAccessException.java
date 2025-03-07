package com.example.CapstoneProject_BE_adoptEasy.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String role) {
        super("Accesso negato: questo metodo è riservato agli utenti con il ruolo " + role + ".");
    }
}
