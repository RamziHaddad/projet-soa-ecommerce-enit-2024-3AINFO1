package com.soa_ecommerce.inventory.handler;


import com.soa_ecommerce.inventory.exception.InsufficientQuantityException;
import com.soa_ecommerce.inventory.exception.ReservationAlreadyCancelledException;
import com.soa_ecommerce.inventory.exception.ReservationAlreadyExistsException;
import com.soa_ecommerce.inventory.exception.ReservationAlreadyReleasedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<String> handleException(InsufficientQuantityException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(ReservationAlreadyExistsException.class)
    public ResponseEntity<String> handleException(ReservationAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(ReservationAlreadyCancelledException.class)
    public ResponseEntity<String> handleException(ReservationAlreadyCancelledException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ReservationAlreadyReleasedException.class)
    public ResponseEntity<String> handleException(ReservationAlreadyReleasedException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }

}
