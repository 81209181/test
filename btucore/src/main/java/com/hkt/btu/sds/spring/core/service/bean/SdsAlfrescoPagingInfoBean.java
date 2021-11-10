package com.hkt.btu.sds.spring.core.service.bean;

public class SdsAlfrescoPagingInfoBean {
	Long count;
	boolean hasMoreItems;
	Long totalItems;
	Long skipCount;
	Long maxItems;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public boolean isHasMoreItems() {
		return hasMoreItems;
	}

	public void setHasMoreItems(boolean hasMoreItems) {
		this.hasMoreItems = hasMoreItems;
	}

	public Long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Long totalItems) {
		this.totalItems = totalItems;
	}

	public Long getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(Long skipCount) {
		this.skipCount = skipCount;
	}

	public Long getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(Long maxItems) {
		this.maxItems = maxItems;
	}
}
