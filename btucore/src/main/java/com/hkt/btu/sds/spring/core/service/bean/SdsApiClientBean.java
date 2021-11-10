package com.hkt.btu.sds.spring.core.service.bean;

import org.springframework.boot.logging.LogLevel;

/**
 * Functional Bean, for storing public assets
 */
public class SdsApiClientBean {
	// constants
	public static final String SEPARATOR = ".";

	public static final String KEY_IDENTIFIER_BASE = "key";
	public static final String KEY_IDENTIFIER = SEPARATOR + KEY_IDENTIFIER_BASE;
	public static final String MAPPING_KEY = "%s" + KEY_IDENTIFIER;

	public static final String LOG_ENABLE_BASE = "logEnable";
	public static final String LOG_ENABLE_IDENTIFIER = SEPARATOR + LOG_ENABLE_BASE;
	public static final String MAPPING_LOG_ENABLE = "%s" + LOG_ENABLE_IDENTIFIER;

	public static final String LOG_LEVEL_BASE = "logLevel";
	public static final String LOG_LEVEL_IDENTIFIER = SEPARATOR + LOG_LEVEL_BASE;
	public static final String MAPPING_LOG_LEVEL = "%s" + LOG_LEVEL_IDENTIFIER;

	private String apiName;
	private String apiKey;
	
	private boolean logEnable = false;
	private LogLevel logLevel = LogLevel.OFF;

	// not implement yet
	// TODO
	private boolean includeHeader = false;
	private boolean includeQuery = true;
	private boolean includeBody = true;

	public boolean isLogEnable() {
		return logEnable;
	}

	public void setLogEnable(boolean logEnable) {
		this.logEnable = logEnable;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public boolean isIncludeHeader() {
		return includeHeader;
	}

	public void setIncludeHeader(boolean includeHeader) {
		this.includeHeader = includeHeader;
	}

	public boolean isIncludeQuery() {
		return includeQuery;
	}

	public void setIncludeQuery(boolean includeQuery) {
		this.includeQuery = includeQuery;
	}

	public boolean isIncludeBody() {
		return includeBody;
	}

	public void setIncludeBody(boolean includeBody) {
		this.includeBody = includeBody;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}
