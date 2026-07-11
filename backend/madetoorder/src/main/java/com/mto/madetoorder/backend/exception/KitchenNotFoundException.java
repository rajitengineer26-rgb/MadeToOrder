package com.mto.madetoorder.backend.exception;

public class KitchenNotFoundException extends RuntimeException {
    public KitchenNotFoundException(String message) {
        super(message);
    }
}
