package com.hkt.btu.common.spring.security.authentication;


import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.core.service.BtuLdapService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuLdapBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.constant.LdapError;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.common.spring.security.exception.ldap.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;
import javax.naming.NamingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class LdapAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final Logger LOG = LogManager.getLogger(LdapAuthenticationProvider.class);

    private static final Map<String, String> LDAP_ERROR_CODE = Collections.unmodifiableMap(
            Arrays.stream(LdapError.values()).collect(Collectors.toMap(LdapError::getCode, LdapError::getMsg)) );

    @Resource (name = "ldapService")
    BtuLdapService ldapService;
    @Resource(name = "auditTrailService")
    BtuAuditTrailService auditTrailService;
    @Resource(name = "userService")
    BtuUserService userService;
    @Resource
    BtuSiteConfigService siteConfigService;

    private BtuUserBean btuUserBean;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final LocalDateTime NOW = LocalDateTime.now();

        BtuUser userDetails = null;
        String userId = auth.getName();
        String loginPwd = auth.getCredentials().toString();
        String domain = Optional.ofNullable(btuUserBean.getLdapDomain())
                .orElseThrow(() -> new LdapNotPermittedLogonException("Invalid LDAP Domain"));
        try {
            // check account enable
            boolean enabled = userService.isEnabled(btuUserBean);

            // check account password expired
            BtuSiteConfigBean btuSiteConfigBean = siteConfigService.getSiteConfigBean();
            Integer passwordLifespanInDay = btuSiteConfigBean.getPasswordLifespanInDay();
            LocalDateTime passwordModifydate = btuUserBean.getPasswordModifydate();
            LocalDateTime passwordExpiryDate = passwordModifydate == null ? null : passwordModifydate.plusDays(passwordLifespanInDay);
            boolean credentialsNonExpired = passwordExpiryDate != null && NOW.isBefore(passwordExpiryDate);

            // check account locked
            boolean accountNonLocked = userService.isNonLocked(btuUserBean);

            userDetails = BtuUser.of(btuUserBean.getUserId(),
                    (String) auth.getCredentials(),
                    enabled,
                    true,
                    credentialsNonExpired,
                    accountNonLocked,
                    btuUserBean.getAuthorities(),
                    btuUserBean);
            userDetails.setLdapPassword((String) auth.getCredentials());

            // check login with ldap
            BtuLdapBean ldapConfig = ldapService.getBtuLdapBean(domain);
            BtuUserBean ldapUserInfo = ldapService.searchUser(ldapConfig, userId, loginPwd, userId);

            // update with ldap info
            String username = StringUtils.isNotEmpty(ldapUserInfo.getUsername()) ? ldapUserInfo.getUsername() : null;
            String ldapEmail = StringUtils.isNotEmpty(ldapUserInfo.getEmail()) ? ldapUserInfo.getEmail() : null;
            userService.updateLdapInfo(userId, username, ldapEmail);

            getPreAuthenticationChecks().check(userDetails);

            //if success, record it
            auditTrailService.insertLoginAuditTrail(userDetails);

            return createSuccessAuthentication(userDetails, auth, userDetails);
        } catch (javax.naming.AuthenticationException authEx) {
            String result;
            String code = "";
            int startIndex = authEx.getExplanation().indexOf("data ");
            int endIndex = 0;

            if (startIndex > 0) {
                endIndex = authEx.getExplanation().indexOf(",", startIndex + 5);
            }
            if (startIndex > 0 && endIndex > startIndex) {
                code = authEx.getExplanation().substring(startIndex + 5, endIndex);
            }

            // log exception
            auditTrailService.insertLoginExceptionAuditTrail(userDetails, LDAP_ERROR_CODE.get(code));
            switch (code) {
                case "525":
                    result = LdapError.USER_NOT_FOUND.getMsg();
                    break;
                case "52e":
                    result = LdapError.INVALID_LOGIN.getMsg();
                    break;
                case "530":
                    result = LdapError.NOT_PERMITTED_LOGIN_TIME.getMsg();
                    throw new LdapNotPermittedLogonException(result);
                case "531":
                    result = LdapError.NOT_PERMITTED_LOGIN_WORKSTATION.getMsg();
                    throw new LdapNotPermittedLogonException(result);
                case "532":
                    result = LdapError.PWD_EXPIRED.getMsg();
                    throw new LdapCredentialsExpiredException(result);
                case "533":
                    result = LdapError.ACCOUNT_DISABLED.getMsg();
                    throw new LdapDisabledException(result);
                case "701":
                    result = LdapError.ACCOUNT_EXPIRED.getMsg();
                    throw new LdapDisabledException(result);
                case "773":
                    result = LdapError.CHANGE_PWD.getMsg();
                    throw new LdapChangePasswordException(result);
                case "775":
                    result = LdapError.LOCK.getMsg();
                    throw new LdapLockedException(result);
                default:
                    result = authEx.getExplanation();
                    break;
            }
            LOG.debug(result);

            throw new LdapBadCredentialsException(result);
        } catch (NamingException e) {
            throw new LdapBadCredentialsException(e.getExplanation());
        } catch (AuthenticationException e) {
            LOG.debug(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new LdapBadCredentialsException(e.getMessage());
        }
    }

    Authentication btuAuth(Authentication auth, BtuUserBean btuUserBean) throws AuthenticationException {
        this.btuUserBean = btuUserBean;
        return authenticate(auth);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        // do nothing
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        return null;
    }
}
