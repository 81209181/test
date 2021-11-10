package com.hkt.btu.common.facade.data;

import org.springframework.data.domain.Sort;

public class BtuPageableData implements DataInterface {

	private int pageNumber;
	private int pageSize;
	private long offset;
	private boolean paged;
	private boolean unpaged;
	private Sort sort;

	public BtuPageableData(int pageNumber, int pageSize, long offset, boolean paged, boolean unpaged, Sort sort) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.offset = offset;
		this.paged = paged;
		this.unpaged = unpaged;
		this.sort = sort;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public boolean isPaged() {
		return paged;
	}

	public void setPaged(boolean paged) {
		this.paged = paged;
	}

	public boolean isUnpaged() {
		return unpaged;
	}

	public void setUnpaged(boolean unpaged) {
		this.unpaged = unpaged;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}
}
