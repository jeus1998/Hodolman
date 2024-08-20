package com.jeulog.exception;

public class InvalidRequest extends JeuException{
    private static final String MESSAGE = "잘못된 요청 입니다.";
    public InvalidRequest() {
        super(MESSAGE);
    }
    public InvalidRequest(String fieldName, String errorMessage) {
        super(MESSAGE);
        super.addValidations(fieldName, errorMessage);
    }
    @Override
    public int statusCode() {
        return 400; // BAD REQUEST
    }
}
