package com.hkt.btu.noc.core.exception;

@SuppressWarnings("unused")
public class InvalidInputException extends RuntimeException {

    public InvalidInputException(){
        this("Invalid input!");
    }

    public InvalidInputException(String message){
        super(message);
    }
}
