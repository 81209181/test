package com.hkt.btu.common.spring.security.authentication;


import com.hkt.btu.common.core.service.authentication.LdapAuthenticationProcessor;
import com.hkt.btu.common.core.service.bean.BtuLdapBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.common.spring.security.exception.ChangePasswordException;
import com.hkt.btu.common.spring.security.exception.NotPermittedLogonException;
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
import java.util.HashSet;
import java.util.Set;


public class LdapAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final Logger LOG = LogManager.getLogger(LdapAuthenticationProvider.class);

    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();


    @Resource
    LdapAuthenticationProcessor ldapAuthProcessor;

    private String domain;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        try {
            BtuUserBean btuUserBean = new BtuUserBean();
            btuUserBean.setUsername((String) auth.getPrincipal());
            btuUserBean.setPassword((String) auth.getCredentials());
            Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
            grantedAuthSet.add(new SimpleGrantedAuthority("ADMIN"));
            grantedAuthSet.add(new SimpleGrantedAuthority("USER"));
            btuUserBean.setAuthorities(grantedAuthSet);
            BtuUser userDetails = BtuUser.of((String) auth.getPrincipal(),
                    (String) auth.getCredentials(),
                    true,
                    true,
                    true,
                    true,
                    grantedAuthSet,
                    btuUserBean);

            preAuthenticationChecks.check(userDetails);
            BtuLdapBean ldapInfo = new BtuLdapBean();
            ldapInfo.setLdapServerUrl("ldaps://ldaps.corphq.hk.pccw.com:636/");
            ldapInfo.setPrincipleName("@corphq.hk.pccw.com");
            ldapAuthProcessor.authenticationOnly(ldapInfo, auth);

            return createSuccessAuthentication(userDetails, auth, userDetails);
        } catch (javax.naming.AuthenticationException authEx) {
            String result = "";
            String code = "";
            int startIndex = authEx.getExplanation().indexOf("data ");
            int endIndex = 0;

            if (startIndex > 0) endIndex = authEx.getExplanation().indexOf(",", startIndex + 5);
            if (startIndex > 0 && endIndex > startIndex)
                code = authEx.getExplanation().substring(startIndex + 5, endIndex);

            if (code.equals("525")) {
                result = "User not found in directory";
            } else if (code.equals("52e")) {
                result = "Invalid login credentials";
            } else if (code.equals("530")) {
                result = "Not permitted to logon at this time. Please contact administrator.";
                throw new NotPermittedLogonException(result);
            } else if (code.equals("531")) {
                result = "Not permitted to logon at this workstation";
                throw new NotPermittedLogonException(result);
            } else if (code.equals("532")) {
                result = "Password has expired. Please change your password.";
                throw new CredentialsExpiredException(result);
            } else if (code.equals("533")) {
                result = "Your account is disabled. Please contact administrator.";
                throw new DisabledException(result);
            } else if (code.equals("701")) {
                result = "Your account has expired. Please contact administrator.";
                throw new DisabledException(result);
            } else if (code.equals("773")) {
                result = "Please change your password.";
                throw new ChangePasswordException(result);
            } else if (code.equals("775")) {
                result = "Your account is locked. Please contact administrator.";
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

    public Authentication btuAuth(Authentication auth, String ldapName) throws AuthenticationException {
        domain = ldapName;
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
