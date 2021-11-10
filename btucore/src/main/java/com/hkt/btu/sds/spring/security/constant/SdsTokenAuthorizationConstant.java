package com.hkt.btu.sds.spring.security.constant;

public class SdsTokenAuthorizationConstant {
	@Deprecated
	public static final String AUTH_HEADER_PREFIX = "Bearer ";
	public static final String AUTH_HEADER_PREFIX_BASIC = "Basic ";
	public static final String AUTH_HEADER_PREFIX_BEARER = "Bearer ";
	//public static final String AUTH_HEADER_PREFIX2 = "Basic "; not support yet
	public static final String AUTH_HEADER_SEPARATOR = ":";
	public static final int TOKEN_POS_API_NAME = 0;
	public static final int TOKEN_POS_API_KEY = 1;
}
