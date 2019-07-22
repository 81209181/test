package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuSessionService;
import com.hkt.btu.common.spring.security.web.authentication.BtuExceptionMappingAuthenticationFailureHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BtuSessionServiceImpl implements BtuSessionService {
    private static final Logger LOG = LogManager.getLogger(BtuSessionServiceImpl.class);

    @Override
    public boolean handleInvalidSession(HttpServletRequest request, HttpServletResponse response,
                                        RuntimeException exception) throws IOException {
        boolean isSessionInvalid = isInvalidSession(request);
        if( isSessionInvalid ){
            String ajaxHeader = request.getHeader("X-Requested-With");
            boolean isAjax = StringUtils.equals("XMLHttpRequest", ajaxHeader);
            if (isAjax) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Please login again.");
                return true;
            } else {
                response.sendRedirect(request.getContextPath() + "/login?error="
                        + BtuExceptionMappingAuthenticationFailureHandler.LOGIN_ERROR.TIMEOUT);
                return true;
            }
        }
        return false;
    }

    private boolean isInvalidSession(HttpServletRequest req) {
        try {
            HttpSession session = req.getSession(false);
            return session == null || !req.isRequestedSessionIdValid();
        }
        catch (IllegalStateException e) {
            LOG.debug(e);
            return true;
        }
    }
}
