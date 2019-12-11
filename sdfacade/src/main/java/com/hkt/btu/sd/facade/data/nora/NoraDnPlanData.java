package com.hkt.btu.sd.facade.data.nora;


public class NoraDnPlanData {

	public static class ACTION{
		public static final String ASSIGNED_CODE = "A";
		public static final String ASSIGNED_DESC = "Assigned";
		public static final String WORKING_CODE = "W";
		public static final String WORKING_DESC = "Working";
		public static final String CLEARED_CODE = "X";
		public static final String CLEARED_DESC = "Cleared";
	}

	private String param = "";
	private String plan = "";
	private String action = "";

	public String getParam()
	{
		return param;
	}

	public void setParam(String param)
	{
		this.param = param;
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