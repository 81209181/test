package com.hkt.btu.common.core.exception;

public class BtuApiCallException extends RuntimeException {
	public BtuApiCallException() {
		this("API call failed.");
	}

	public BtuApiCallException(String message) {
		super(message);
	}
}
