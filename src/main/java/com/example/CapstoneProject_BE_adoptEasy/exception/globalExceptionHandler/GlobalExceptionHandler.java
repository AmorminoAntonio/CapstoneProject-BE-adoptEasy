package com.example.CapstoneProject_BE_adoptEasy.exception.globalExceptionHandler;

import com.example.CapstoneProject_BE_adoptEasy.exception.AnimaleFoundException;
import com.example.CapstoneProject_BE_adoptEasy.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AnimaleFoundException.class)
    public ResponseEntity<String> handleAnimaleNotFoundException(AnimaleFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);  // Restituisce un errore 404 con il messaggio personalizzato
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);  // Restituisce un errore 403 con il messaggio personalizzato
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);  // Restituisce un errore 400 con il messaggio di errore generico
    }
}
