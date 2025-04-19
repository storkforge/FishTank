package org.example.fishtank.exception.custom;

public class CurrentUserException extends RuntimeException {

    public CurrentUserException(String message) {
        super(message);
    }
}
