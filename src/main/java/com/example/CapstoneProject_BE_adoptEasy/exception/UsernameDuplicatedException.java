package com.example.CapstoneProject_BE_adoptEasy.exception;

public class UsernameDuplicatedException extends RuntimeException {
    public UsernameDuplicatedException(String message) {
        super(message);
    }
}
