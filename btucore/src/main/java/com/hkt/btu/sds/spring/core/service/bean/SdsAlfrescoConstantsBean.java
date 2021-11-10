package com.hkt.btu.sds.spring.core.service.bean;

import java.time.format.DateTimeFormatter;

public class SdsAlfrescoConstantsBean {
	public static final String LOGIN_API = "/alfresco/service/api/login";
	public static final String FILE_BASE_API = "/alfresco/api/-default-/public/alfresco/versions/1/nodes";
	public static final String API_FILE_CHILDREN_SUFFIX = "/children";
	public static final String API_FILE_CONTENT_SUFFIX = "/content";
	public static final String API_FILE_VERSION_SUFFIX = "/versions";
	public static final String API_MOVE_NODE_SUFFIX = "/move";

	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);

	public static final String ASPECT_NAME_VERSIONING = "cm:versionable";

	public static final String PROPERTY_VERSION_LABEL = "cm:versionLabel";
	public static final String PROPERTY_VERSION_TYPE = "cm:versionType";

	public static final String PARAM_MAJOR_VERSION = "majorVersion";
	public static final String PARAM_AUTO_RENAME = "autoRename";
	public static final String PARAM_FILE_NAME = "name";
	public static final String PARAM_OFFSET = "skipCount";
	public static final String PARAM_LIMIT = "maxItems";
	public static final String PARAM_RELATIVE_PATH = "relativePath";

	public static final String NODE_TYPE_FOLDER = "cm:folder";
}
