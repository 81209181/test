package com.hkt.btu.sd.core.exception;

@SuppressWarnings("unused")
public class AccessRequestNotFoundException extends RuntimeException {
    public AccessRequestNotFoundException(){
        this("Access request not found!");
    }

    public AccessRequestNotFoundException(String message){
        super(message);
    }
}
