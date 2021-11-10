package com.hkt.btu.common.core.exception;

public class BtuMissingImplException extends RuntimeException {
    public BtuMissingImplException() {
        this("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }

    public BtuMissingImplException(String message) {
        super(message);
    }
}
