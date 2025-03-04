package com.example.CapstoneProject_BE_adoptEasy.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrStatusResponse {
    private HttpStatus httpStatus;
    private String message;
}
