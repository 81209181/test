package com.hkt.btu.common.core.exception;

@SuppressWarnings("unused")
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(){
        this("Invalid input!");
    }

    public InvalidInputException(String message){
        super(message);
    }
}
