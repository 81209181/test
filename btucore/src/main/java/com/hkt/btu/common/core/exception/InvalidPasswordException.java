package com.hkt.btu.common.core.exception;

@SuppressWarnings("unused")
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(){
        this("Invalid password!");
    }

    public InvalidPasswordException(String message){
        super(message);
    }
}
