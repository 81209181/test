package com.hkt.btu.common.spring.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Value("${servicedesk.api.headerKey}")
    private String headerKey;
    @Value("${servicedesk.api.headerValue}")
    private String headerValue;
    @Value("${servicedesk.api.tokenUsername}")
    private String tokenUsername;
    @Value("${servicedesk.api.tokenPassword}")
    private String tokenPassword;

    @Autowired
    SessionRegistry sessionRegistry;

    public TokenAuthenticationFilter() {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String token = httpServletRequest.getHeader(this.headerKey);
        if (ObjectUtils.isEmpty(token)) {
            throw new AuthenticationCredentialsNotFoundException("Access Token is not provided.");
        }
        if (!token.equalsIgnoreCase(headerValue)) {
            throw new BadCredentialsException("Token is not match.");
        }
        Assert.notNull(tokenUsername, "Token username must not be null.");
        Assert.notNull(tokenPassword, "Token password must not be null.");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tokenUsername, tokenPassword);
        authenticationToken.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication) && authentication.isAuthenticated()) {
            return false;
        }
        if (ObjectUtils.isEmpty(request.getContentType()) || !request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            return false;
        }
        return super.requiresAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request,response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        String message;
        if (ObjectUtils.isEmpty(failed.getCause())) {
            message = failed.getMessage();
        } else {
            message = failed.getCause().getMessage();
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("message", message);
        response.getOutputStream().write(mapper.writeValueAsBytes(node));
    }
}
