package com.hkt.btu.sds.spring.core.api.alfresco.response;

import com.hkt.btu.sds.spring.core.service.bean.SdsAlfrescoFileSearchListHolderBean;

public class SdsAlfrescoFileSearchListResponse {
	private SdsAlfrescoFileSearchListHolderBean list;

	public SdsAlfrescoFileSearchListHolderBean getList() {
		return list;
	}

	public void setList(SdsAlfrescoFileSearchListHolderBean list) {
		this.list = list;
	}
}
