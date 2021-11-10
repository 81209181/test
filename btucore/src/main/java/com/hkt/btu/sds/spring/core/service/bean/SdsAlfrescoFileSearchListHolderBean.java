package com.hkt.btu.sds.spring.core.service.bean;

import java.util.List;

public class SdsAlfrescoFileSearchListHolderBean {
	private SdsAlfrescoPagingInfoBean pagination;
	private List<SdsAlfrescoFileSearchListDetailBean> entries;

	public SdsAlfrescoPagingInfoBean getPagination() {
		return pagination;
	}

	public void setPagination(SdsAlfrescoPagingInfoBean pagination) {
		this.pagination = pagination;
	}

	public List<SdsAlfrescoFileSearchListDetailBean> getEntries() {
		return entries;
	}

	public void setEntries(List<SdsAlfrescoFileSearchListDetailBean> entries) {
		this.entries = entries;
	}
}
