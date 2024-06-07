package com.example.fintechapplication.errorResponse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private int statusCode;
}

