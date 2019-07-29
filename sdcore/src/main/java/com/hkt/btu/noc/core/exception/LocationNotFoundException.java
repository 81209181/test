package com.hkt.btu.noc.core.exception;

@SuppressWarnings("unused")
public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(){
        this("Location not found!");
    }

    public LocationNotFoundException(String message){
        super(message);
    }
}
