package com.hkt.btu.common.spring.security.access;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

import static com.hkt.btu.common.spring.security.access.intercept.BtuSecurityMetadataSource.CONFIG_ATTR_DENY_ALL;
import static com.hkt.btu.common.spring.security.access.intercept.BtuSecurityMetadataSource.CONFIG_ATTR_PERMIT_ALL;

public class BtuAccessDecisionManager implements AccessDecisionManager {
    private static final Logger LOG = LogManager.getLogger(BtuAccessDecisionManager.class);

    // pass when fulfill any of the required user group config
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if( CollectionUtils.isEmpty(configAttributes) ){
            LOG.warn("No user group requirement config found.");
            throw new AccessDeniedException("No user group requirement config found.");
        } else if ( authentication==null ){
            throw new AccessDeniedException("No authentication found.");
        }

        // loop all required user group config
        for (ConfigAttribute configAttribute : configAttributes) {
            if (CONFIG_ATTR_DENY_ALL.equals(configAttribute)) {
                continue;
            } else if (CONFIG_ATTR_PERMIT_ALL.equals(configAttribute)) {
                return; // pass
            }

            String requiredUserGroup = configAttribute.getAttribute();

            // check all user groups in user
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                String userUserGroup = ga.getAuthority();
                if (StringUtils.equals(requiredUserGroup, userUserGroup)) {
                    LOG.debug(String.format("User passes user group config [%s] with user group [%s]",
                            requiredUserGroup, userUserGroup));
                    return; // pass
                }
            }
        }

        // fail, meeting NONE of the user group config
        LOG.debug("Insufficient User Group！");
        throw new AccessDeniedException("Insufficient User Group！");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof SecurityConfig;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
