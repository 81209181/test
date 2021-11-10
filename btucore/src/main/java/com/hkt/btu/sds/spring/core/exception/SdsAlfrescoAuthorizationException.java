package com.hkt.btu.sds.spring.core.exception;

public class SdsAlfrescoAuthorizationException extends RuntimeException{
	public SdsAlfrescoAuthorizationException() {
	}

	public SdsAlfrescoAuthorizationException(String message) {
		super(message);
	}

	public SdsAlfrescoAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SdsAlfrescoAuthorizationException(Throwable cause) {
		super(cause);
	}

	protected SdsAlfrescoAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
