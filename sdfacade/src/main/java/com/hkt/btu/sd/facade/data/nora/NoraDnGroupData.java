package com.hkt.btu.sd.facade.data.nora;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoraDnGroupData
{
	private String asCluster = "";
	private String adminPortalId = "";
	private String cliNumber = "";
	private String recordingProvisionMethod = "";
	private String dod = "";
	private int cac = 0;
	ArrayList<NoraDnPlanData> userPlanList = new ArrayList<NoraDnPlanData>();

	public String getAsCluster()
	{
		return asCluster;
	}

	public void setAsCluster(String asCluster)
	{
		this.asCluster = asCluster;
	}

	public String getAdminPortalId()
	{
		return adminPortalId;
	}

	public void setAdminPortalId(String adminPortalId)
	{
		this.adminPortalId = adminPortalId;
	}

	public String getCliNumber()
	{
		return cliNumber;
	}

	public void setCliNumber(String cliNumber)
	{
		this.cliNumber = cliNumber;
	}

	public String getRecordingProvisionMethod()
	{
		return recordingProvisionMethod;
	}

	public void setRecordingProvisionMethod(String recordingProvisionMethod)
	{
		this.recordingProvisionMethod = recordingProvisionMethod;
	}

	public String getDod()
	{
		return dod;
	}

	public void setDod(String dod)
	{
		this.dod = dod;
	}

	public int getCac()
	{
		return cac;
	}

	public void setCac(int cac)
	{
		this.cac = cac;
	}

	public ArrayList<NoraDnPlanData> getUserPlanList()
	{
		return userPlanList;
	}

	public void setUserPlanList(ArrayList<NoraDnPlanData> userPlanList)
	{
		this.userPlanList = userPlanList;
	}
}