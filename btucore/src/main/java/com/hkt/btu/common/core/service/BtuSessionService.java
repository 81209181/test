package com.hkt.btu.common.core.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface BtuSessionService {
    boolean handleInvalidSession(HttpServletRequest request, HttpServletResponse response, RuntimeException exception) throws IOException;
}
