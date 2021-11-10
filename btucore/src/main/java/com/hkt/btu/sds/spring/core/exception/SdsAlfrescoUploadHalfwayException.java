package com.hkt.btu.sds.spring.core.exception;

public class SdsAlfrescoUploadHalfwayException extends RuntimeException {
	private String createdFileId;

	public SdsAlfrescoUploadHalfwayException(String message, String createdFileId) {
		super(message);
		this.createdFileId = createdFileId;
	}

	public SdsAlfrescoUploadHalfwayException(String message, String createdFileId, Throwable cause) {
		super(message, cause);
		this.createdFileId = createdFileId;
	}

	public SdsAlfrescoUploadHalfwayException(String createdFileId, Throwable cause) {
		super(cause);
		this.createdFileId = createdFileId;
	}

	protected SdsAlfrescoUploadHalfwayException(String message, String createdFileId, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.createdFileId = createdFileId;
	}

	public String getCreatedFileId() {
		return createdFileId;
	}

	public void setCreatedFileId(String createdFileId) {
		this.createdFileId = createdFileId;
	}
}
