package com.hkt.btu.common.core.exception;

@SuppressWarnings("unused")
public class InsufficientAuthorityException extends RuntimeException {
    public InsufficientAuthorityException(){
        this("Insufficient Authority!");
    }

    public InsufficientAuthorityException(String message){
        super(message);
    }
}
