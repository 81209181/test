package com.hkt.btu.common.spring.security.access.intercept;

import com.hkt.btu.common.spring.security.access.BtuAccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import java.io.IOException;

public class BtuSecurityInterceptor extends FilterSecurityInterceptor implements Filter {

    @Resource(name = "customBtuSecurityMetadataSource")
    private BtuSecurityMetadataSource btuSecurityMetadataSource;

    @Resource(name = "customBtuAccessDecisionManager")
    public void setMyAccessDecisionManager(BtuAccessDecisionManager btuAccessDecisionManager) {
        super.setAccessDecisionManager(btuAccessDecisionManager);
    }

    @Override
    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.finallyInvocation(token);
        }
        super.afterInvocation(token, null);
    }

    @Override
    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return this.btuSecurityMetadataSource;
    }
    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.btuSecurityMetadataSource;
    }
    @Override
    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource newSource) {
        // forbidden to set metadata source from outside
    }

}
