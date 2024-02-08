package com.lucifer.electronics.store.exceptions;

public class BadApiRequestException extends RuntimeException {

    public BadApiRequestException() {
        super();
    }

    public BadApiRequestException(String message) {
        super(message);
    }
}
