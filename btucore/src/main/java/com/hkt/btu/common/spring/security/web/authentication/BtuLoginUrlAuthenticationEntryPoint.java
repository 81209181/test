package com.hkt.btu.common.spring.security.web.authentication;

import com.hkt.btu.common.core.service.BtuHttpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

// ref: https://stackoverflow.com/questions/23901950/spring-security-ajax-session-timeout-issue
public class BtuLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public static final String LOGIN_URI = "/login";
    public static final String OAUTH2_LOGIN_URI = "/oauth";
    public static final String LOGIN_ERROR_PARA_URI = LOGIN_URI + "?error=";
    public static final Map<String, String> ERROR_LIST = Collections
            .unmodifiableMap(Arrays.stream(LOGIN_ERROR.values())
                    .collect(Collectors.toMap
                            (constant -> constant.name(), constant -> constant.msg)));


    public enum LOGIN_ERROR {
        LOCK("Account deactivated. Please contact admin."),
        BAD_CREDENTIALS("Invalid login credential."),
        FORBIDDEN("Please login again. (Timeout/Forbidden)."),
        TIMEOUT("Please login again. (Timeout)"),
        LOGIN("Please login."),
        NO_AUTH("Please login. (Auth not found)"),
        INSUFFICIENT_AUTH("Insufficient Authority."),
        HELP("Please contact system support."),
        UNKNOWN("Login error."),
        DISABLED("Your account is disabled/expired. Please contact administrator."),

        NOT_PERMITTED("Domain (LDAP): Not permitted to logon at this time or this workstation. Please contact administrator."),
        CHANGE_PWD("Domain (LDAP): Please change your password."),
        LDAP_LOCK("Domain (LDAP): Account deactivated. Please contact admin."),
        LDAP_BAD_CREDENTIALS("Domain (LDAP): Invalid login credential."),
        LDAP_DISABLED("Domain (LDAP): Your account is disabled/expired. Please contact administrator."),
        LDAP_CREDENTIALS_EXPIRED("Domain (LDAP): Your password was expired.");

        private String msg;

        LOGIN_ERROR(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public String getUri() {
            return LOGIN_ERROR_PARA_URI + this.name();
        }

        public static String getValue(String name) {
            return StringUtils.isEmpty(ERROR_LIST.get(name)) ? UNKNOWN.msg : ERROR_LIST.get(name);
        }
    }


    @Resource(name = "httpService")
    private BtuHttpService btuHttpService;

    public BtuLoginUrlAuthenticationEntryPoint() {
        super(LOGIN_ERROR.LOGIN.getUri());
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        boolean isHandled = btuHttpService.handleKnownAuthDenied(request, response, authException);
        if(isHandled){
            return;
        }

        super.commence(request, response, authException);
    }
}
