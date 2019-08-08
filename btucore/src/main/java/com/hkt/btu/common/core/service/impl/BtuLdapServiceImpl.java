package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.BtuLdapService;
import com.hkt.btu.common.core.service.bean.BtuLdapBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.javax.net.LdapSSLSocketFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class BtuLdapServiceImpl implements BtuLdapService {

    private final static Logger LOG = LogManager.getLogger(BtuLdapServiceImpl.class);

    @Override
    public void authenticationOnly(BtuLdapBean ldapInfo, Authentication auth) throws NamingException {
        String userName = auth.getName();
        final String ldapURL = ldapInfo.getLdapServerUrl();
        final String dn =  userName + "@" +ldapInfo.getPrincipleName();
        final String pwd = auth.getCredentials().toString();
        DirContext ctx = null;
        try {
            ctx = getContext(ldapURL, dn, pwd);
        } finally {
            if (ctx != null) ctx.close();
        }
    }

    @Override
    public BtuUserBean searchUser(BtuLdapBean ldapInfo, String username, String password, String staffId) throws NamingException {
        final String ldapURL = ldapInfo.getLdapServerUrl();
        final String dn = username + ldapInfo.getPrincipleName();
        DirContext ctx = null;
        try {
            ctx = getContext(ldapURL, dn, password);
            String filter = "(&(objectclass=user)(!(userAccountControl:1.2.840.113556.1.4.803:=2))(|(userPrincipalName=" + staffId + ldapInfo.getPrincipleName() + ")" + "(mailNickname="
                    + staffId + ")" + "(extensionAttribute1=" + staffId + ")))";
            return getSearchResult(ldapInfo, ctx, filter);
        } catch (UserNotFoundException e) {
            LOG.error("User not found: " + staffId + "\nLDAPURL: " + ldapURL);
            throw e;
        } finally {
            if (ctx != null) ctx.close();
        }
    }

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

    private DirContext getContext(final String ldapURL, final String dn, final String password) throws NamingException {
        // Access the keystore, this is where the Root CA public key cert was installed
        // Could also do this via the command line option java -Djavax.net.ssl.trustStore....
        // No need to specifiy the keystore password for read operations
        // String keystore = "C:\\Program Files\\Java\\jre6\\lib\\security\\cacerts";
        // System.setProperty("javax.net.ssl.trustStore", keystore);

        Hashtable<String, String> authEnv = new Hashtable<String, String>(7);
        authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        authEnv.put(Context.PROVIDER_URL, ldapURL);
        authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        authEnv.put(Context.SECURITY_PRINCIPAL, dn);
        authEnv.put(Context.SECURITY_CREDENTIALS, password);

        // For LDAPS
        if (ldapURL.startsWith("ldaps")) {
            authEnv.put(Context.SECURITY_PROTOCOL, "ssl");
            authEnv.put("java.naming.ldap.factory.socket", LdapSSLSocketFactory.class.getCanonicalName());
        }

        try {
            return new InitialDirContext(authEnv);

        } catch (javax.naming.AuthenticationException authEx) {
            throw authEx;
        } catch (NamingException namEx) {
            namEx.printStackTrace();
            throw namEx;
        } finally {
            //if (ctx != null) ctx.close();
        }
    }

    protected BtuUserBean getSearchResult(BtuLdapBean ldapInfo, DirContext ctx, String filter) throws NamingException {
        try {
            String ldapBase = ldapInfo.getLdapAttributeLoginName();
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> results = ctx.search(ldapBase, filter, constraints);

            if (results != null && results.hasMore()) {
                Map<String, String> ldapResponse = getLdapResponseAttrMap(results);
                return null;
            } else {
                throw new UserNotFoundException("User not found in LDAP domain");
            }
        } finally {

        }
    }
}
