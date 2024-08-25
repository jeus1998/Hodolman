package com.jeulog.exception;

public class InvalidSignInformation extends JeuException {
    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다";
    public InvalidSignInformation() {
        super(MESSAGE);
    }
    @Override
    public int statusCode() {
        return 400;
    }
}
