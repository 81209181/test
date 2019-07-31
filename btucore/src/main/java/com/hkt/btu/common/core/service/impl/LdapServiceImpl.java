package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.LdapService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import java.util.HashMap;
import java.util.Map;

public class LdapServiceImpl implements LdapService {

    private final static Logger LOG = LogManager.getLogger(LdapServiceImpl.class);

    @Override
    public Map<String, String> getLdapResponseAttrMap(NamingEnumeration response) throws NamingException {
        if (response == null) {
            LOG.warn("LDAP response is null.");
            return null;
        } else if (!response.hasMore()) {
            LOG.warn("LDAP response is empty.");
            return null;
        }

        Object resultObj = response.next();
        if (!(resultObj instanceof SearchResult)) {
            LOG.warn("LDAP response is not an SearchResult.");
            return null;
        }

        SearchResult searchResult = (SearchResult) resultObj;
        Attributes attributes = searchResult.getAttributes();
        if (attributes == null) {
            LOG.warn("LDAP response result attributes is null.");
            return null;
        }

        Map<String, String> ldapAttrMap = new HashMap<>();
        for (NamingEnumeration ne = attributes.getAll(); ne.hasMoreElements(); ) {
            Attribute attr = (Attribute) ne.next();
            Object val = attr.get();
            if (val instanceof String) {
                ldapAttrMap.put(attr.getID(), (String) val);
                LOG.debug(attr.getID() + ": " + val);
            }
        }
        return ldapAttrMap;
    }
}
