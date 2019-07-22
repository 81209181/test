package com.hkt.btu.common.spring.security.access;

import com.hkt.btu.common.core.service.BtuSessionService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BtuAccessDeniedHandler extends AccessDeniedHandlerImpl {

    @Resource(name = "sessionService")
    private BtuSessionService btuSessionService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
            throws IOException, ServletException {
        boolean isHandled = btuSessionService.handleInvalidSession(request, response, exception);
        if(isHandled){
            return;
        }

        super.handle(request, response, exception);
    }


}
