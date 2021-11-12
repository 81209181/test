package com.hkt.btu.sd.core.exception;

@SuppressWarnings("unused")
public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException(){
        this("Invalid otp!");
    }

    public InvalidOtpException(String message){
        super(message);
    }
}
