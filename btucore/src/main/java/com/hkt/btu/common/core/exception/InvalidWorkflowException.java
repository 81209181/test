package com.hkt.btu.common.core.exception;

@SuppressWarnings("unused")
public class InvalidWorkflowException extends RuntimeException {
    public InvalidWorkflowException(){
        this("Invalid workflow!");
    }

    public InvalidWorkflowException(String message){
        super(message);
    }
}
