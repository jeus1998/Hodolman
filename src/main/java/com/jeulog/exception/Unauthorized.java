package com.jeulog.exception;

/**
 * status -> 401
 */
public class Unauthorized extends JeuException{
    private static final String MESSAGE = "인증이 필요합니다.";
    public Unauthorized() {
        super(MESSAGE);
    }
    public Unauthorized(String fieldName, String errorMessage) {
        super(MESSAGE);
        super.addValidations(fieldName, errorMessage);
    }
    @Override
    public int statusCode() {
        return 401;
    }
}
