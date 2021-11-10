package com.hkt.btu.common.core.exception;

public class CacheNotFoundException extends RuntimeException {
    public CacheNotFoundException(){
        this("Cache not found!");
    }

    public CacheNotFoundException(String message){
        super(message);
    }
}
