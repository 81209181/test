package com.hkt.btu.common.spring.security.authentication;


import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.core.service.BtuLdapService;
import com.hkt.btu.common.core.service.bean.BtuLdapBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.constant.LdapError;
import com.hkt.btu.common.core.service.constant.LdapEnum;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.common.spring.security.exception.ChangePasswordException;
import com.hkt.btu.common.spring.security.exception.NotPermittedLogonException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import javax.annotation.Resource;
import javax.naming.NamingException;
import java.util.*;
import java.util.stream.Collectors;


public class LdapAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final Logger LOG = LogManager.getLogger(LdapAuthenticationProvider.class);

    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();

    private static final Map<String, String> LDAP_ERROR_CODE = Collections.unmodifiableMap
            (Arrays.stream(LdapError.values()).
                    collect(Collectors.toMap(constant -> constant.getCode(), constant -> constant.getMsg())));

    @Resource
    BtuLdapService btuLdapService;

    @Resource(name = "auditTrailService")
    BtuAuditTrailService auditTrailService;

    private String domain;

    private BtuUserBean btuUserBean;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        BtuUser userDetails = null;
        try {
            Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
            grantedAuthSet.add(new SimpleGrantedAuthority("ADMIN"));
            grantedAuthSet.add(new SimpleGrantedAuthority("USER"));
            btuUserBean.setAuthorities(grantedAuthSet);
            userDetails = BtuUser.of(btuUserBean.getUserId(),
                    (String) auth.getCredentials(),
                    true,
                    true,
                    true,
                    true,
                    grantedAuthSet,
                    btuUserBean);
            userDetails.setLdapPassword((String) auth.getCredentials());
            // Prepare ldap data
            BtuLdapBean ldapInfo = getBtuLdapBean(userDetails.getUserBean().getLdapDomain());

            // login ldap
            btuLdapService.authenticationOnly(ldapInfo, auth);

            //if success, record it
            auditTrailService.insertLoginAuditTrail(userDetails);

            return createSuccessAuthentication(userDetails, auth, userDetails);
        } catch (javax.naming.AuthenticationException authEx) {
            String result = "";
            String code = "";
            int startIndex = authEx.getExplanation().indexOf("data ");
            int endIndex = 0;

            if (startIndex > 0) endIndex = authEx.getExplanation().indexOf(",", startIndex + 5);
            if (startIndex > 0 && endIndex > startIndex)
                code = authEx.getExplanation().substring(startIndex + 5, endIndex);

            // log exception
            auditTrailService.insertLoginExceptionAuditTrail(userDetails, LDAP_ERROR_CODE.get(code));
            if (code.equals("525")) {
                result = LdapError.USER_NOT_FOUND.getMsg();
            } else if (code.equals("52e")) {
                result = LdapError.INVALID_LOGIN.getMsg();
            } else if (code.equals("530")) {
                result = LdapError.NOT_PERMITTED_LOGIN_TIME.getMsg();
                throw new NotPermittedLogonException(result);
            } else if (code.equals("531")) {
                result = LdapError.NOT_PERMITTED_LOGIN_WORKSTATION.getMsg();
                throw new NotPermittedLogonException(result);
            } else if (code.equals("532")) {
                result = LdapError.PWD_EXPIRED.getMsg();
                throw new CredentialsExpiredException(result);
            } else if (code.equals("533")) {
                result = LdapError.ACCOUNT_DISABLED.getMsg();
                throw new DisabledException(result);
            } else if (code.equals("701")) {
                result = LdapError.ACCOUNT_EXPIRED.getMsg();
                throw new DisabledException(result);
            } else if (code.equals("773")) {
                result = LdapError.CHANGE_PWD.getMsg();
                throw new ChangePasswordException(result);
            } else if (code.equals("775")) {
                result = LdapError.LOCK.getMsg();
                throw new LockedException(result);
            } else result = authEx.getExplanation();
            LOG.debug(result);

            throw new BadCredentialsException(result);
        } catch (NamingException e) {
            throw new BadCredentialsException(e.getExplanation());
        } catch (AuthenticationException e) {
            LOG.debug(e.getMessage());
            throw e;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new BadCredentialsException(e.getMessage());
        } finally {
            domain = null;
        }
    }

    public Authentication btuAuth(Authentication auth, BtuUserBean btuUserBean, String ldapName) throws AuthenticationException {
        this.domain = ldapName;
        this.btuUserBean = btuUserBean;
        return authenticate(auth);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // do nothing

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        //return userDetailService.loadUserByUsername(username);
        return null;
    }

    public BtuLdapBean getBtuLdapBean(String domain) {
        BtuLdapBean ldapInfo = new BtuLdapBean();
        switch (domain) {
            case "@corphq.hk.pccw.com":
                ldapInfo.setLdapServerUrl(LdapEnum.PCCW.getHostUrl());
                ldapInfo.setLdapAttributeLoginName(LdapEnum.PCCW.getBase());
                break;
            case "@corp.root":
                ldapInfo.setLdapServerUrl(LdapEnum.PCCWS.getHostUrl());
                ldapInfo.setLdapAttributeLoginName(LdapEnum.PCCWS.getBase());
                break;
            case "@oa.hkcsl.net":
                ldapInfo.setLdapServerUrl(LdapEnum.CSL.getHostUrl());
                ldapInfo.setLdapAttributeLoginName(LdapEnum.CSL.getBase());
                break;
            default:
                return null;
        }
        ldapInfo.setPrincipleName(domain);
        return ldapInfo;
    }


    // copy from AbstractUserDetailsAuthenticationProvider
    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.debug("User account is locked");

                throw new LockedException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"));
            }

            if (!user.isEnabled()) {
                logger.debug("User account is disabled");

                throw new DisabledException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"));
            }

            if (!user.isAccountNonExpired()) {
                logger.debug("User account is expired");

                throw new AccountExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"));
            }
        }
    }
}
