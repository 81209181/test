package com.hkt.btu.common.facade.data;

public class BtuGeneralSearchColumnData {
	private String name;
	private boolean orderable = false;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOrderable() {
		return orderable;
	}
	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}
}
