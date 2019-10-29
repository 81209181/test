package com.hkt.btu.sd.facade.data.nora;

import com.hkt.btu.common.facade.data.DataInterface;

public class NoraAccountData implements DataInterface
{
	private String accountId = "";
	private String password = "";

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}