package com.hkt.btu.common.core.service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface BtuHttpService {
    boolean handleKnownAuthDenied(HttpServletRequest request, HttpServletResponse response, Exception exception)
            throws IOException;
}
