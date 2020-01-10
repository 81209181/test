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
        // check user token
        String username = (String) usernamePasswordAuthenticationToken.getPrincipal();
        String password = (String) usernamePasswordAuthenticationToken.getCredentials();
        boolean validToken = apiClientService.checkApiClientKey(username, password);
        if (!validToken) {
            throw new BadCredentialsException("Invalid token.");
        }

        // check user
        BtuUserBean userBean = userService.getUserBeanByUsername(username);
        boolean isActiveUser = userService.isActive(userBean);


        if (isActiveUser) {
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
