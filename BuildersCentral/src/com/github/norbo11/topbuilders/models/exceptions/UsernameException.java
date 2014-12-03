package com.github.norbo11.topbuilders.models.exceptions;

public class UsernameException extends Exception {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMessage() {
        return "Invalid username.";
    }
}
