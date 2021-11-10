package com.hkt.btu.sds.spring.core.api.alfresco.response;

import com.hkt.btu.sds.spring.core.service.bean.SdsAlfrescoTicketBean;

public class SdsAlfrescoLoginResponse {
	SdsAlfrescoTicketBean data;

	public SdsAlfrescoTicketBean getData() {
		return data;
	}

	public void setData(SdsAlfrescoTicketBean data) {
		this.data = data;
	}
}
