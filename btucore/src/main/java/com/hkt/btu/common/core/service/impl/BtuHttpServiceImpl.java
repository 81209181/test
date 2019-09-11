package com.hkt.btu.common.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hkt.btu.common.core.exception.InsufficientUserGroupException;
import com.hkt.btu.common.core.exception.MissingRequiredUserGroupException;
import com.hkt.btu.common.core.exception.NoAuthFoundException;
import com.hkt.btu.common.core.service.BtuHttpService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.hkt.btu.common.spring.security.web.authentication.BtuLoginUrlAuthenticationEntryPoint.LOGIN_ERROR;
import static com.hkt.btu.common.spring.security.web.authentication.BtuLoginUrlAuthenticationEntryPoint.LOGIN_ERROR_PARA_URI;

public class BtuHttpServiceImpl implements BtuHttpService {
    private static final Logger LOG = LogManager.getLogger(BtuHttpServiceImpl.class);

    @Override
    public boolean handleKnownAuthDenied(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException {
        // check request info
        boolean isAjax = StringUtils.equals("XMLHttpRequest", request.getHeader("X-Requested-With"));
        String contextPath = request.getContextPath();

        if (!ObjectUtils.isEmpty(request.getContentType())){
            if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
                ObjectMapper mapper =new ObjectMapper();
                ObjectNode node = mapper.createObjectNode();
                node.put("message",exception.getMessage());
                response.getOutputStream().write(mapper.writeValueAsBytes(node));
                return true;
            }
        }

        // handle invalid session
        boolean isSessionInvalid = isInvalidSession(request);
        if( isSessionInvalid ){
            buildResponse(response, isAjax, LOGIN_ERROR.TIMEOUT.name(), getLoginRedirectUri(contextPath, LOGIN_ERROR.TIMEOUT) );
            return true;
        }

        // handle known AccessDeniedException
        if(exception instanceof AccessDeniedException) {
            if (exception instanceof InsufficientUserGroupException) {
                LOG.debug("Insufficient User Group！");
                buildResponse(response, isAjax, LOGIN_ERROR.INSUFFICIENT_AUTH.name(), getLoginRedirectUri(contextPath, LOGIN_ERROR.INSUFFICIENT_AUTH) );
                return true;
            } else if (exception instanceof MissingRequiredUserGroupException) {
                LOG.error("No user group requirement config found.");
                LOG.error(request.getRequestURI());
                buildResponse(response, isAjax, LOGIN_ERROR.HELP.name(), getLoginRedirectUri(contextPath, LOGIN_ERROR.HELP) );
                return true;
            } else if (exception instanceof NoAuthFoundException) {
                LOG.debug("No Auth Found.");
                buildResponse(response, isAjax, LOGIN_ERROR.NO_AUTH.name(), getLoginRedirectUri(contextPath, LOGIN_ERROR.NO_AUTH) );
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

    private void buildResponse(HttpServletResponse response, boolean isAjax, String ajaxMsg, String httpRedirect) throws IOException {
        if (isAjax) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ajaxMsg);
        } else {
            response.sendRedirect(httpRedirect);
        }
    }

    private String getLoginRedirectUri(String contextPath, LOGIN_ERROR errorEnum){
        return contextPath + LOGIN_ERROR_PARA_URI + errorEnum.name();
    }
}
