package com.irctc.exception;

public class ResourceNotFoundException extends RuntimeException{
    private String message;

    public ResourceNotFoundException(String message){
        super(message);
    }
}
