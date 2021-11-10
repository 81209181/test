package com.hkt.btu.sds.spring.core.api.alfresco.request;

public class SdsAlfrescoMoveNodeRequest {
	private String targetParentId;
	private String name;

	public java.lang.String getTargetParentId() {
		return targetParentId;
	}

	public void setTargetParentId(java.lang.String targetParentId) {
		this.targetParentId = targetParentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
