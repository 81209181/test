package com.hkt.btu.sds.spring.core.exception;

public class SdsAlfrescoApiException extends RuntimeException{
	public SdsAlfrescoApiException() {
	}

	public SdsAlfrescoApiException(String message) {
		super(message);
	}

	public SdsAlfrescoApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public SdsAlfrescoApiException(Throwable cause) {
		super(cause);
	}

	protected SdsAlfrescoApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
