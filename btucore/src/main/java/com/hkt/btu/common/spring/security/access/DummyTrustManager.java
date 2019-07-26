package com.hkt.btu.common.spring.security.access;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class DummyTrustManager implements X509TrustManager
{
	public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException
	{
		// do nothing
	}

	public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException
	{
		// do nothing
	}

	public X509Certificate[] getAcceptedIssuers()
	{
		return new X509Certificate[0];
	}
}