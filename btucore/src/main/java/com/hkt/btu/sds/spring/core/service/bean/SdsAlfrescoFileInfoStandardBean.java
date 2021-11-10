package com.hkt.btu.sds.spring.core.service.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SdsAlfrescoFileInfoStandardBean {
	private boolean isFile;
	private boolean isFolder;
	private String id;
	private String name;
	private String nodeType;
	private String parentId;

	private SdsAlfrescoFileMimeBean content;

	private LocalDateTime modifiedAt;
	private LocalDateTime createdAt;

	private SdsAlfrescoUserInfoBean createdByUser;
	private SdsAlfrescoUserInfoBean modifiedByUser;

	List<String> aspectNames;
	Map<String, Object> properties;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public SdsAlfrescoUserInfoBean getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(SdsAlfrescoUserInfoBean createdByUser) {
		this.createdByUser = createdByUser;
	}

	public SdsAlfrescoUserInfoBean getModifiedByUser() {
		return modifiedByUser;
	}

	public void setModifiedByUser(SdsAlfrescoUserInfoBean modifiedByUser) {
		this.modifiedByUser = modifiedByUser;
	}

	public List<String> getAspectNames() {
		return aspectNames;
	}

	public void setAspectNames(List<String> aspectNames) {
		this.aspectNames = aspectNames;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public SdsAlfrescoFileMimeBean getContent() {
		return content;
	}

	public void setContent(SdsAlfrescoFileMimeBean content) {
		this.content = content;
	}
}
