package com.hkt.btu.sd.core.exception;

@SuppressWarnings("unused")
public class InvalidWorkflowException extends RuntimeException {
    public InvalidWorkflowException(){
        this("Invalid workflow!");
    }

    public InvalidWorkflowException(String message){
        super(message);
    }
}
