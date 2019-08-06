package com.hkt.btu.sd.core.exception;

@SuppressWarnings("unused")
public class AuthorityNotFoundException extends RuntimeException {
    public AuthorityNotFoundException(){
        this("Authority not found!");
    }

    public AuthorityNotFoundException(String message){
        super(message);
    }
}
