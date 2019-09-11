package com.hkt.btu.common.spring.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.Base64Utils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public TokenAuthenticationFilter() {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = "";
        List<String> list;
        if (!ObjectUtils.isEmpty(header)) {
            if(header.startsWith("Bearer ")){
                token = header.substring(7);
            }
        } else {
            throw new BadCredentialsException("token not found.");
        }
        try {
            list = Arrays.asList(StringUtils.trimAllWhitespace(new String(Base64Utils.decodeFromString(token))).split(":"));
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("token not match.");
        }
        if (list.size()<2) {
            throw new BadCredentialsException("token not match.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(list.get(0),list.get(1));
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
        chain.doFilter(request, response);
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
