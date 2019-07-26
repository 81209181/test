package com.hkt.btu.common.spring.security.authentication;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.bean.BtuLdapBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.spring.security.access.LdapSSLSocketFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

public class LdapAuthenticationProcessor {

	private static final Logger LOG = LogManager.getLogger(LdapAuthenticationProcessor.class);


	
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
	
//	public List<BtuUserBean> searchUsers(PpmsLdapEntity ldapInfo, Authentication auth, String... staffIds) throws NamingException {
//		String username = auth.getPrincipal().toString();
//		final String ldapURL = ldapInfo.getLdapServerUrl();
//		final String dn = username + ldapInfo.getPrincipleName();
//		final String pwd = auth.getCredentials().toString();
//		final String domainGroup = ldapInfo.getLdapAttributeLoginName();
//
//		int i = 0;
//		DirContext ctx = null;
//		try {
//			ctx = getContext(ldapURL,dn,pwd);
//			BtuUserBean bean = null;
//			for (; i< staffIds.length; i++) {
//				String staffId = staffIds[i];
//				String filter = "(&(objectclass=user)(|(userPrincipalName=" + staffId + ldapInfo.getPrincipleName() + ")" + "(mailNickname="
//						+ staffId + ")" + "(extensionAttribute1=" + staffId + ")))";
//				bean = getSearchResult(ctx, domainGroup, filter);
//				if (bean != null)
//					list.add(bean);
//			}
//		} catch (UserNotFoundException e) {
//			LOG.error("User not found: " + staffIds[i] + "\nLDAPURL: " + ldapURL);
//			throw e;
//		} finally {
//			if (ctx != null) ctx.close();
//		}
//	}
	
	private DirContext getContext(final String ldapURL, final String  dn, final String password) throws NamingException
	{
//		final String ldapBase = SiteManager.getCurrentSiteInfo().getLdapAttributeLoginName();
//		final String ldapURL = SiteManager.getCurrentSiteInfo().getLdapServerUrl();
//		final String dn = requestorUserName + SiteManager.getCurrentSiteInfo().getPrincipalName();
//		String filter = "(&(objectclass=user)(|(userPrincipalName=" + userName + SiteManager.getCurrentSiteInfo().getPrincipalName() + ")" + "(mailNickname="
//		        + userName + ")" + "(extensionAttribute1=" + userName + ")))";

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
			// authEx.printStackTrace();

			// 525 user not found
			// 52e invalid credentials
			// 530 not permitted to logon at this time
			// 531 not permitted to logon at this workstation
			// 532 password expired
			// 533 account disabled
			// 701 account expired
			// 773 user must reset password
			// 775 user account locked

//			String result = "";
//			String code = "";
//			int startIndex = authEx.getExplanation().indexOf("data ");
//			int endIndex = 0;
//
//			if (startIndex > 0) endIndex = authEx.getExplanation().indexOf(",", startIndex + 5);
//			if (startIndex > 0 && endIndex > startIndex) code = authEx.getExplanation().substring(startIndex + 5, endIndex);
//
//			if (code.equals("525"))
//				result = "User not found in directory";
//			else if (code.equals("52e"))
//				result = "Invalid login credentials";
//			else if (code.equals("530"))
//				result = "Not permitted to logon at this time. Please contact administrator.";
//			else if (code.equals("531"))
//				result = "Not permitted to logon at this workstation";
//			else if (code.equals("532"))
//				result = "Password has expired. Please change your password.";
//			else if (code.equals("533"))
//				result = "Your account is disabled. Please contact administrator.";
//			else if (code.equals("701"))
//				result = "Your account has expired. Please contact administrator.";
//			else if (code.equals("773"))
//				result = "Please change your password.";
//			else if (code.equals("775"))
//				result = "Your account is locked. Please contact administrator.";
//			else result = authEx.getExplanation();
//
//			throw new RuntimeException(result);
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
				SearchResult sr = (SearchResult) results.next();
				return null;
				//return ppmsUserService.getUserBeanBySearchResult(sr, ldapInfo);
//				Attributes attrs = sr.getAttributes();
//				userBean = new UserBean();
//				userBean.setUserName(userName);

//				for (NamingEnumeration<? extends Attribute> ne = attrs.getAll(); ne.hasMoreElements();)
//				{
//					Attribute attr = (Attribute) ne.next();
//					String attrID = attr.getID();
//
//					if ("givenName".equals(attrID))
//						userBean.setFirstName(getFirstElement(attr));
//					else if ("initials".equals(attrID))
//						userBean.setChineseName(getFirstElement(attr));
//					else if ("sn".equals(attrID))
//						userBean.setLastName(getFirstElement(attr));
//					else if ("title".equals(attrID))
//						userBean.setJobTitle(getFirstElement(attr));
//					else if ("company".equals(attrID))
//						userBean.setCompany(getFirstElement(attr));
//					else if ("department".equals(attrID))
//						userBean.setBranch(getFirstElement(attr));
//					else if ("physicalDeliveryOfficeName".equals(attrID))
//						userBean.setLocation(getFirstElement(attr));
//					else if ("telephoneNumber".equals(attrID))
//						userBean.setOfficePhone(getFirstElement(attr));
//					else if ("mobile".equals(attrID))
//						userBean.setMobile(getFirstElement(attr));
//					else if ("facsimileTelephoneNumber".equals(attrID))
//						userBean.setOfficeFax(getFirstElement(attr));
//					else if ("mail".equals(attrID))
//						userBean.setMsMailAddress(getFirstElement(attr));
//					else if ("extensionAttribute2".equals(attrID)) userBean.setCcc(getFirstElement(attr));
//				}
			} else {
				throw new UserNotFoundException("User not found in LDAP domain");
			}
		}
		finally
		{
			
		}
	}
}
