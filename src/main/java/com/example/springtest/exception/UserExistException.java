package com.example.springtest.exception;

public class UserExistException extends Throwable{

    public UserExistException(final String message) {
        super(message);
    }
}
