package com.hkt.btu.sd.core.exception;

@SuppressWarnings("unused")
public class InvalidUserTypeException extends RuntimeException {
    public InvalidUserTypeException(){
        this("Invalid userType!");
    }

    public InvalidUserTypeException(String message){
        super(message);
    }
}
