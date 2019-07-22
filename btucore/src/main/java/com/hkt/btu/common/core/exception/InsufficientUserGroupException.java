package com.hkt.btu.common.core.exception;

import org.springframework.security.access.AccessDeniedException;

public class InsufficientUserGroupException extends AccessDeniedException {

    public InsufficientUserGroupException() {
        super("Insufficient user group.");
    }

}
