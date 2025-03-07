package com.example.CapstoneProject_BE_adoptEasy.exception;

public class AnimaleFoundException extends RuntimeException {
    public AnimaleFoundException(long id) {
        super("Animale con ID " + id + " non trovato nel sistema.");
    }
}
