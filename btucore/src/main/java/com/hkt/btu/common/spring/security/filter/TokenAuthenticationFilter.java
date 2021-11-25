package com.hkt.btu.common.spring.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hkt.btu.sds.spring.security.constant.SdsTokenAuthorizationConstant;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

@SuppressWarnings("deprecating")
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // it is an customized API-auth token authorization. Naming is not good enough. moved as SdsApiTokenAuthenticationFilter

    // incoming http Authorization header: Bearer V0ZNOmY3ZDBkOWQ5LTdkOTEtNDZlOS04NDk0LTlhNTFhOTFkZDY2Yg==
    private boolean clearAuthAfterSuccess = true;


    public TokenAuthenticationFilter() {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        String apiName = deserializeApiName(header);
        if(apiName==null){
            throw new BadCredentialsException("Invalid Authorization API Name.");
        }

        String apiKey = deserializeApiKey(header);
        if(apiKey==null){
            throw new BadCredentialsException("Invalid Authorization API Key.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(apiName, apiKey);
        return getAuthenticationManager().authenticate(authenticationToken);
    }


    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!ObjectUtils.isEmpty(authentication) && authentication.isAuthenticated()) {
            return false;
        }
        if (ObjectUtils.isEmpty(request.getContentType()) || !StringUtils.equalsAnyIgnoreCase(
                StringUtils.deleteWhitespace(request.getContentType()),
                new String[]{MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})) {
            return false;
        }
        return super.requiresAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        try {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);
            chain.doFilter(request, response);
        } finally {
            if (clearAuthAfterSuccess) {
                SecurityContext context = SecurityContextHolder.getContext();
                if (context != null)
                    context.setAuthentication(null);
                SecurityContextHolder.clearContext();
            }
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
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

    private String deserializeAuthHeader(String authorizationHeader, int pos){
        try {
            if (!StringUtils.startsWith(authorizationHeader, SdsTokenAuthorizationConstant.AUTH_HEADER_PREFIX)) {
                return null;
            }
            String serializeToken = StringUtils.substringAfter(authorizationHeader, SdsTokenAuthorizationConstant.AUTH_HEADER_PREFIX);
            String deserializeToken = new String(Base64Utils.decodeFromString(serializeToken));
            String[] tokenArray = StringUtils.split(deserializeToken, SdsTokenAuthorizationConstant.AUTH_HEADER_SEPARATOR);
            return ArrayUtils.getLength(tokenArray) < pos ? null : tokenArray[pos];
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    private String deserializeApiName(String authorizationHeader) {
        return deserializeAuthHeader(authorizationHeader, SdsTokenAuthorizationConstant.TOKEN_POS_API_NAME);
    }

    private String deserializeApiKey(String authorizationHeader) {
        return deserializeAuthHeader(authorizationHeader, SdsTokenAuthorizationConstant.TOKEN_POS_API_KEY);
    }

    public boolean isClearAuthAfterSuccess() {
        return clearAuthAfterSuccess;
    }

    public void setClearAuthAfterSuccess(boolean clearAuthAfterSuccess) {
        this.clearAuthAfterSuccess = clearAuthAfterSuccess;
    }
}