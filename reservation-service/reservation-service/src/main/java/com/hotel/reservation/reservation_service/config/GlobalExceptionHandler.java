package com.hotel.reservation.reservation_service.config;

   import com.hotel.reservation.reservation_service.exception.NoDataFoundException;
   import org.springframework.http.HttpStatus;
   import org.springframework.http.ResponseEntity;
   import org.springframework.web.bind.annotation.ControllerAdvice;
   import org.springframework.web.bind.annotation.ExceptionHandler;
   import org.springframework.web.context.request.WebRequest;

   @ControllerAdvice
   public class GlobalExceptionHandler {

       @ExceptionHandler(NoDataFoundException.class)
       public ResponseEntity<String> handleNoDataFoundException(NoDataFoundException ex, WebRequest request) {
           return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
       }
   }