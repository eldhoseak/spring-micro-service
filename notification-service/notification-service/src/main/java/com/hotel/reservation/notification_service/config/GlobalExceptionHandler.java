package com.hotel.reservation.notification_service.config;

import com.hotel.reservation.notification_service.exception.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<String> handleNoDataFoundException(NoDataFoundException ex, WebRequest request) {
        // Return the error message and a 404 Not Found status
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}