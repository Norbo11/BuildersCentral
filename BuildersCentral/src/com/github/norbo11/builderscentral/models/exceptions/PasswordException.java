package com.github.norbo11.builderscentral.models.exceptions;

public class PasswordException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "Invalid password.";
    }
}
