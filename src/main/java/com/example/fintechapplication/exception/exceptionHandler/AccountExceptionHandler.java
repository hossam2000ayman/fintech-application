package com.example.fintechapplication.exception.exceptionHandler;

import com.example.fintechapplication.errorResponse.ErrorResponse;
import com.example.fintechapplication.exception.AccountNotFoundException;
import com.example.fintechapplication.exception.InsufficientBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException accountNotFoundException) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(accountNotFoundException.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFundException(InsufficientBalanceException insufficientBalanceException) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(insufficientBalanceException.getMessage());
        errorResponse.setStatus(HttpStatus.NOT_ACCEPTABLE);
        errorResponse.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
}
