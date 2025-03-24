package com.example.CapstoneProject_BE_adoptEasy.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage {
    private String status;
    private String message;
}
