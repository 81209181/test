package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuLdapBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import org.springframework.security.core.Authentication;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.Map;

public interface BtuLdapService {

    void authenticationOnly(BtuLdapBean ldapInfo, Authentication auth) throws NamingException;

    BtuUserBean searchUser(BtuLdapBean ldapInfo, String username, final String password, String staffId) throws NamingException;

    Map<String, String> getLdapResponseAttrMap(NamingEnumeration response) throws NamingException;

    BtuLdapBean getBtuLdapBean(String domain);
}
