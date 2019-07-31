package com.hkt.btu.common.core.service;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.Map;

public interface LdapService {

    Map<String, String> getLdapResponseAttrMap(NamingEnumeration response) throws NamingException;
}
