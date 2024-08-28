package com.jeulog.exception;

public class AlreadyExistsEmailException extends JeuException{
    private static final String MESSAGE = "이미 가입된 이메일입니다.";
    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }
    @Override
    public int statusCode() {
        return 400;
    }
}
