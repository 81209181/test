package com.hkt.btu.sd.core.exception;

@SuppressWarnings("unused")
public class AccessRequestVisitorNotFoundException extends RuntimeException {
    public AccessRequestVisitorNotFoundException(){
        this("Access request visitor not found!");
    }

    public AccessRequestVisitorNotFoundException(String message){
        super(message);
    }
}
