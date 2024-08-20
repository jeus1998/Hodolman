package com.jeulog.exception;

public abstract class JeuException extends RuntimeException {
    public JeuException(String message) {
        super(message);
    }
    public abstract int statusCode();
}
