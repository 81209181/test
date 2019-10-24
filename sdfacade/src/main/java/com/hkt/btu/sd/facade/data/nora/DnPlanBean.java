package com.hkt.btu.sd.facade.data.nora;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DnPlanBean
{
	@JsonProperty("param")
	private String dn = "";
	private String plan = "";
	private String action = "";

	public String getDn()
	{
		return dn;
	}

	public void setDn(String dn)
	{
		this.dn = dn;
	}

	public String getPlan()
	{
		return plan;
	}

	public void setPlan(String plan)
	{
		this.plan = plan;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}
}