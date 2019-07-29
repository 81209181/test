package com.hkt.btu.noc.core.exception;

@SuppressWarnings({"unused", "WeakerAccess"})
public class DuplicateUserEmailException extends RuntimeException {
    public DuplicateUserEmailException(){
        this("User email already existed!");
    }

    public DuplicateUserEmailException(String message){
        super(message);
    }
}
