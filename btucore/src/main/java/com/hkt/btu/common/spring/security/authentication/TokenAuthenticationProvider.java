package com.hkt.btu.common.spring.security.authentication;

import com.hkt.btu.common.core.service.BtuApiClientService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;

public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Resource(name = "userService")
    BtuUserService userService;
    @Resource(name = "apiClientService")
    BtuApiClientService apiClientService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        String username = (String) usernamePasswordAuthenticationToken.getPrincipal();
        String password = (String) usernamePasswordAuthenticationToken.getCredentials();
        String decryptApiClient = apiClientService.getApiClientBean(username);
        boolean isNotMatch = true;
        if (password.equals(decryptApiClient)) {
            isNotMatch =false;
        }
        if (isNotMatch) {
            throw new BadCredentialsException("token is not match!");
        }
        BtuUserBean userBean = userService.getUserBeanByUsername(username);
        if (userService.isActive(userBean)) {
            return BtuUser.of(
                    userBean.getUsername(),
                    userBean.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    userBean.getAuthorities(),
                    userBean);
        } else {
            throw new BadCredentialsException("User is not active!");
        }
    }
}
