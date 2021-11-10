package com.hkt.btu.common.core.service.bean;

import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;

public class BtuParamBean extends BaseBean {
	private BtuConfigParamTypeEnum paramType;
	private String value;


	public BtuConfigParamTypeEnum getParamType() {
		return paramType;
	}

	public void setParamType(BtuConfigParamTypeEnum paramType) {
		this.paramType = paramType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
