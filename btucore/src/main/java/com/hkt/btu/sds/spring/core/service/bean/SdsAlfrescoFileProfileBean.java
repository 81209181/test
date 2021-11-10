package com.hkt.btu.sds.spring.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class SdsAlfrescoFileProfileBean extends BaseBean {

	//public static final Double DEFAULT_VERSION = 1.0d;
	public static class VERSION_TYPE {
		public static final String MAJOR = "MAJOR";
		public static final String MINOR = "MINOR";
	}

	// always available from Alfresco
	private String fileId;
	private String parentId;
	private boolean isFile;
	private boolean isFolder;

	private String fileName;
	private String nodeType;
	private String mimeType;
	private Long size;


	// may not available from Alfresco
	private Double version;
	private String versionType;

	List<String> aspects = new ArrayList<>();
	// default up to 100 records
	List<SdsAlfrescoFileVersionInfoBean> versions = new ArrayList<>();

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean file) {
		isFile = file;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean folder) {
		isFolder = folder;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public List<String> getAspects() {
		return aspects;
	}

	public void setAspects(List<String> aspects) {
		this.aspects = aspects;
	}

	public List<SdsAlfrescoFileVersionInfoBean> getVersions() {
		return versions;
	}

	public void setVersions(List<SdsAlfrescoFileVersionInfoBean> versions) {
		this.versions = versions;
	}
}
