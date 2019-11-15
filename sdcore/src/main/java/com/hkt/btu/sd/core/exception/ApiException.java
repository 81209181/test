package com.hkt.btu.sd.core.exception;

public class ApiException extends RuntimeException {

    public ApiException(){
        this("WFM API error!");
    }

    public ApiException(String message){
        super(message);
    }
}
