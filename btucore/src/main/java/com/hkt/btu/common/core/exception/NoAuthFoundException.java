package com.hkt.btu.common.core.exception;

import org.springframework.security.access.AccessDeniedException;

public class NoAuthFoundException extends AccessDeniedException {
    public NoAuthFoundException(){
        super("No authentication found.");
    }
}
