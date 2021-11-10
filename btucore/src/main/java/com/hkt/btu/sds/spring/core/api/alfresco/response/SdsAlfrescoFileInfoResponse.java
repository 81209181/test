package com.hkt.btu.sds.spring.core.api.alfresco.response;

import com.hkt.btu.sds.spring.core.service.bean.SdsAlfrescoFileInfoStandardBean;

public class SdsAlfrescoFileInfoResponse {
	private SdsAlfrescoFileInfoStandardBean entry;

	public SdsAlfrescoFileInfoStandardBean getEntry() {
		return entry;
	}

	public void setEntry(SdsAlfrescoFileInfoStandardBean entry) {
		this.entry = entry;
	}
}
