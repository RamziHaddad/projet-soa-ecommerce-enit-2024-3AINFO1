package com.soa_ecommerce.inventory.exception;

public class ReservationAlreadyReleasedException extends RuntimeException {
    public ReservationAlreadyReleasedException(String message) {
        super(message);
    }
}
