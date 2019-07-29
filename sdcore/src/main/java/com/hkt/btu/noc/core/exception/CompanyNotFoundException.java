package com.hkt.btu.noc.core.exception;

@SuppressWarnings("unused")
public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(){
        this("Company not found!");
    }

    public CompanyNotFoundException(String message){
        super(message);
    }
}
