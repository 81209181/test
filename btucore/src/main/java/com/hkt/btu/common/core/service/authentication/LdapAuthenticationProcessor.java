package com.hkt.btu.common.core.service.authentication;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.LdapService;
import com.hkt.btu.common.core.service.bean.BtuLdapBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.javax.net.LdapSSLSocketFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;
import java.util.Map;

public class LdapAuthenticationProcessor {

	private static final Logger LOG = LogManager.getLogger(LdapAuthenticationProcessor.class);

	@Resource
	LdapService ldapService;
	
	public void authenticationOnly(BtuLdapBean ldapInfo, Authentication auth) throws NamingException
	{
		String username = auth.getPrincipal().toString();
		final String ldapURL = ldapInfo.getLdapServerUrl();
		final String dn = username + ldapInfo.getPrincipleName();
		final String pwd = auth.getCredentials().toString();
		DirContext ctx = null;
		try {
			 ctx = getContext(ldapURL,dn,pwd);
		} finally {
			if (ctx != null) ctx.close();
		}
	}
	
	public BtuUserBean searchUser(BtuLdapBean ldapInfo, String username, final String password, String staffId) throws NamingException {
//		String username = ((BtuUser) auth.getPrincipal()).getUserBean();
		final String ldapURL = ldapInfo.getLdapServerUrl();
		final String dn = username + ldapInfo.getPrincipleName();
		DirContext ctx = null;
		try {
			ctx = getContext(ldapURL,dn,password);
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
	

	private DirContext getContext(final String ldapURL, final String  dn, final String password) throws NamingException
	{
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
		if (ldapURL.startsWith("ldaps"))
		{
			authEnv.put(Context.SECURITY_PROTOCOL, "ssl");
			authEnv.put("java.naming.ldap.factory.socket", LdapSSLSocketFactory.class.getCanonicalName());
		}

		try
		{
			return new InitialDirContext(authEnv);
			
		}
		catch (javax.naming.AuthenticationException authEx)
		{
			throw authEx;
		}
		catch (NamingException namEx)
		{
			namEx.printStackTrace();
			throw namEx;
		}
		finally
		{
			//if (ctx != null) ctx.close();
		}
	}
	
	protected BtuUserBean getSearchResult(BtuLdapBean ldapInfo, DirContext ctx, String filter) throws NamingException
	{
		try
		{
			String ldapBase = ldapInfo.getLdapAttributeLoginName();
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> results = ctx.search(ldapBase, filter, constraints);

			if (results != null && results.hasMore())
			{
				Map<String, String> ldapResponse = ldapService.getLdapResponseAttrMap(results);
				return null;
			} else {
				throw new UserNotFoundException("User not found in LDAP domain");
			}
		}
		finally
		{
			
		}
	}
}
