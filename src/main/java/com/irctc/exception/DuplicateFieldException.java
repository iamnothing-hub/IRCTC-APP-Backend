package com.irctc.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



public class DuplicateFieldException extends RuntimeException{

    private String message;

    public DuplicateFieldException(String message){
        super(message);
    }
}
