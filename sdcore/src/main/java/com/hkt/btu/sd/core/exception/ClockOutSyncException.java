package com.hkt.btu.sd.core.exception;

public class ClockOutSyncException extends RuntimeException {
    public ClockOutSyncException() {
        this("Clock out of sync!");
    }

    public ClockOutSyncException(String message) {
        super(message);
    }
}