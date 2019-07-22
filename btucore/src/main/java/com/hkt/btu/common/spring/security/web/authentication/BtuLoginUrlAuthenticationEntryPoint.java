package com.hkt.btu.common.spring.security.web.authentication;

import com.hkt.btu.common.core.service.BtuHttpService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// ref: https://stackoverflow.com/questions/23901950/spring-security-ajax-session-timeout-issue
public class BtuLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public static final String LOGIN_URI = "/login";
    public static final String LOGIN_ERROR_PARA_URI = LOGIN_URI + "?error=";

    public enum LOGIN_ERROR {
        LOCK("Account deactivated. Please contact admin."),
        BAD_CREDENTIALS("Invalid login credential."),
        FORBIDDEN("Please login again. (Timeout/Forbidden)."),
        TIMEOUT("Please login again. (Timeout)"),
        LOGIN("Please login."),
        NO_AUTH("Please login. (Auth not found)"),
        INSUFFICIENT_AUTH("Insufficient Authority."),
        HELP("Please contact system support."),
        UNKNOWN("Login error.")
        ;

        private String msg;

        LOGIN_ERROR(String msg){
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public String getUri(){
            return LOGIN_ERROR_PARA_URI + this.name();
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
