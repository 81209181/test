package com.hkt.btu.common.core.exception;

import org.springframework.security.access.AccessDeniedException;

public class MissingRequiredUserGroupException extends AccessDeniedException {
    public MissingRequiredUserGroupException(){
        super("Missing required user group.");
    }
}
