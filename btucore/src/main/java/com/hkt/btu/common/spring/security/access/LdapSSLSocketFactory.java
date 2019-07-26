package com.hkt.btu.common.spring.security.access;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;

public class LdapSSLSocketFactory extends SocketFactory
{
	private SSLSocketFactory socketFactory;

	public LdapSSLSocketFactory()
	{
		try
		{
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]
			{ new DummyTrustManager() }, new SecureRandom());
			socketFactory = ctx.getSocketFactory();
		}
		catch (Exception ex)
		{
			ex.printStackTrace(System.err);
		}
	}

	public static SocketFactory getDefault()
	{
		return new LdapSSLSocketFactory();
	}

	@Override
	public Socket createSocket(String string, int i) throws IOException, UnknownHostException
	{
		return socketFactory.createSocket(string, i);
	}

	@Override
	public Socket createSocket(String string, int i, InetAddress ia, int i1) throws IOException, UnknownHostException
	{
		return socketFactory.createSocket(string, i, ia, i1);
	}

	@Override
	public Socket createSocket(InetAddress ia, int i) throws IOException
	{
		return socketFactory.createSocket(ia, i);
	}

	@Override
	public Socket createSocket(InetAddress ia, int i, InetAddress ia1, int i1) throws IOException
	{
		return socketFactory.createSocket(ia, i, ia1, i1);
	}
}
