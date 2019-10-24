package com.hkt.btu.sd.facade.data.nora;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hkt.btu.common.facade.data.DataInterface;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PidInfoBean implements DataInterface
{
	private int pid = 0;
	private String stb = null;
	private String description = null;

	public int getPid()
	{
		return pid;
	}

	public void setPid(int pid)
	{
		this.pid = pid;
	}

	public String getStb()
	{
		return stb;
	}

	public void setStb(String stb)
	{
		this.stb = stb;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}