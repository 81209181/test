package com.hkt.btu.common.core.exception;

@SuppressWarnings("unused")
public class AuthorityNotFoundException extends RuntimeException {
    public AuthorityNotFoundException(){
        this("Authority not found!");
    }

    public AuthorityNotFoundException(String message){
        super(message);
    }
}
