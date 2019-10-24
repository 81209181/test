package com.hkt.btu.sd.facade.data.nora;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pccw.dwfm.util.UtilBase;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressInfoBean
{
	private String addrId = ""; // The unique address ID of the address which is provided by TSMS
	private String addr1 = ""; // Flat/room/house no
	private String addr2 = ""; // Lot number
	private String addr3 = ""; // Floor
	private String addr4 = ""; // Block
	private String addr5 = ""; // Building name
	private String addr6 = ""; // Phase1
	private String addr7 = ""; // Phase2
	private String addr8 = ""; // Estate Name
	private String addr9 = ""; // Start Street number
	private String addr10 = ""; // End Street number
	private String addr11 = ""; // Street Name and Category
	private String addr12 = ""; // District
	private String addr13 = ""; // Region Hong Kong, Kowloon, NT
	private String addr14 = ""; // Grid ID, region ID defined by HKT internally
	private String addr15 = ""; // Exchange building ID
	private String addr16 = ""; // Building ID
	private String addr17 = ""; // SB service boundary
	private String addr18 = ""; // 2N Indicator
	private String addr19 = ""; // BMO Indicator
	private String addr20 = ""; // Blockage
	private String addr21 = ""; // Housing Type
	private String addr22 = ""; // Market Zone
	private String addr23 = ""; // Street Type
	private String addr24 = ""; // Section Name
	private String addr25 = ""; // Risk Level
	private String addr26 = ""; // SB Address
	private String addr27 = ""; // Address Sequence
	// The address blacklist check status
	// 0: This address is not in blacklist;
	// 1: This address is in blacklist but the order is still submitted to NOSS;
	// 2: The address blacklist check is not available at this moment but the order is still submitted to NOSS
	private String blacklistCheck = "";

	@JsonIgnore
	public String getAddressString1()
	{
		String result = UtilBase.appendStringWithDelimiter("", addr1, ", ", "FLAT %s");
		result = UtilBase.appendStringWithDelimiter(result, addr2, ", ", "LOT %s");
		result = UtilBase.appendStringWithDelimiter(result, addr3, ", ", "%s/F");
		result = UtilBase.appendStringWithDelimiter(result, addr4, ", ", "BLOCK %s");

		result = UtilBase.appendStringWithDelimiter(result, addr5, ", ", "%s");
		result = UtilBase.appendStringWithDelimiter(result, addr6, ", ", "%s");
		result = UtilBase.appendStringWithDelimiter(result, addr7, ", ", "%s");
		result = UtilBase.appendStringWithDelimiter(result, addr8, ", ", "%s");

		return result;
	}

	@JsonIgnore
	public String getAddressString2()
	{
		// Street number and name
		String street = UtilBase.appendStringWithDelimiter("", addr9, "", "%s");
		street = UtilBase.appendStringWithDelimiter(street, addr10, " ", "- %s");
		street = UtilBase.appendStringWithDelimiter(street, addr11, " ", "%s");
		return UtilBase.appendStringWithDelimiter("", street, ", ", "%s");
	}

	@JsonIgnore
	public String getAddressString3()
	{
		String result = UtilBase.appendStringWithDelimiter("", addr12, ", ", "%s");
		result = UtilBase.appendStringWithDelimiter(result, addr13, ", ", "%s");

		return result;
	}

	@JsonIgnore
	public String getAddressString()
	{
		String result = UtilBase.appendStringWithDelimiter("", addr1, ", ", "FLAT %s");
		result = UtilBase.appendStringWithDelimiter(result, addr2, ", ", "LOT %s");
		result = UtilBase.appendStringWithDelimiter(result, addr3, ", ", "%s/F");
		result = UtilBase.appendStringWithDelimiter(result, addr4, ", ", "BLOCK %s");

		result = UtilBase.appendStringWithDelimiter(result, addr5, ", ", "%s");
		result = UtilBase.appendStringWithDelimiter(result, addr6, ", ", "%s");
		result = UtilBase.appendStringWithDelimiter(result, addr7, ", ", "%s");
		result = UtilBase.appendStringWithDelimiter(result, addr8, ", ", "%s");

		// Street number and name
		String street = UtilBase.appendStringWithDelimiter("", addr9, "", "%s");
		street = UtilBase.appendStringWithDelimiter(street, addr10, " ", "- %s");
		street = UtilBase.appendStringWithDelimiter(street, addr11, " ", "%s");
		result = UtilBase.appendStringWithDelimiter(result, street, ", ", "%s");

		result = UtilBase.appendStringWithDelimiter(result, addr12, ", ", "%s");
		result = UtilBase.appendStringWithDelimiter(result, addr13, ", ", "%s");

		return result;
	}

	public String getAddrId()
	{
		return addrId;
	}

	public void setAddrId(String addrId)
	{
		this.addrId = addrId;
	}

	public String getAddr1()
	{
		return addr1;
	}

	public void setAddr1(String addr1)
	{
		this.addr1 = addr1;
	}

	public String getAddr2()
	{
		return addr2;
	}

	public void setAddr2(String addr2)
	{
		this.addr2 = addr2;
	}

	public String getAddr3()
	{
		return addr3;
	}

	public void setAddr3(String addr3)
	{
		this.addr3 = addr3;
	}

	public String getAddr4()
	{
		return addr4;
	}

	public void setAddr4(String addr4)
	{
		this.addr4 = addr4;
	}

	public String getAddr5()
	{
		return addr5;
	}

	public void setAddr5(String addr5)
	{
		this.addr5 = addr5;
	}

	public String getAddr6()
	{
		return addr6;
	}

	public void setAddr6(String addr6)
	{
		this.addr6 = addr6;
	}

	public String getAddr7()
	{
		return addr7;
	}

	public void setAddr7(String addr7)
	{
		this.addr7 = addr7;
	}

	public String getAddr8()
	{
		return addr8;
	}

	public void setAddr8(String addr8)
	{
		this.addr8 = addr8;
	}

	public String getAddr9()
	{
		return addr9;
	}

	public void setAddr9(String addr9)
	{
		this.addr9 = addr9;
	}

	public String getAddr10()
	{
		return addr10;
	}

	public void setAddr10(String addr10)
	{
		this.addr10 = addr10;
	}

	public String getAddr11()
	{
		return addr11;
	}

	public void setAddr11(String addr11)
	{
		this.addr11 = addr11;
	}

	public String getAddr12()
	{
		return addr12;
	}

	public void setAddr12(String addr12)
	{
		this.addr12 = addr12;
	}

	public String getAddr13()
	{
		return addr13;
	}

	public void setAddr13(String addr13)
	{
		this.addr13 = addr13;
	}

	public String getAddr14()
	{
		return addr14;
	}

	public void setAddr14(String addr14)
	{
		this.addr14 = addr14;
	}

	public String getAddr15()
	{
		return addr15;
	}

	public void setAddr15(String addr15)
	{
		this.addr15 = addr15;
	}

	public String getAddr16()
	{
		return addr16;
	}

	public void setAddr16(String addr16)
	{
		this.addr16 = addr16;
	}

	public String getAddr17()
	{
		return addr17;
	}

	public void setAddr17(String addr17)
	{
		this.addr17 = addr17;
	}

	public String getAddr18()
	{
		return addr18;
	}

	public void setAddr18(String addr18)
	{
		this.addr18 = addr18;
	}

	public String getAddr19()
	{
		return addr19;
	}

	public void setAddr19(String addr19)
	{
		this.addr19 = addr19;
	}

	public String getAddr20()
	{
		return addr20;
	}

	public void setAddr20(String addr20)
	{
		this.addr20 = addr20;
	}

	public String getAddr21()
	{
		return addr21;
	}

	public void setAddr21(String addr21)
	{
		this.addr21 = addr21;
	}

	public String getAddr22()
	{
		return addr22;
	}

	public void setAddr22(String addr22)
	{
		this.addr22 = addr22;
	}

	public String getAddr23()
	{
		return addr23;
	}

	public void setAddr23(String addr23)
	{
		this.addr23 = addr23;
	}

	public String getAddr24()
	{
		return addr24;
	}

	public void setAddr24(String addr24)
	{
		this.addr24 = addr24;
	}

	public String getAddr25()
	{
		return addr25;
	}

	public void setAddr25(String addr25)
	{
		this.addr25 = addr25;
	}

	public String getAddr26()
	{
		return addr26;
	}

	public void setAddr26(String addr26)
	{
		this.addr26 = addr26;
	}

	public String getAddr27()
	{
		return addr27;
	}

	public void setAddr27(String addr27)
	{
		this.addr27 = addr27;
	}

	public String getBlacklistCheck()
	{
		return blacklistCheck;
	}

	public void setBlacklistCheck(String blacklistCheck)
	{
		this.blacklistCheck = blacklistCheck;
	}

	public boolean isSameAddress(AddressInfoBean otherAddressInfoBean)
	{
		if (otherAddressInfoBean == null) return false;

		if (UtilBase.isNotEmptyValue(addrId) && addrId.equals(otherAddressInfoBean.getAddrId()))
			return true;
		else if (addr1.equals(otherAddressInfoBean.getAddr1()) && addr2.equals(otherAddressInfoBean.getAddr2()) && addr3.equals(otherAddressInfoBean.getAddr3())
		        && addr4.equals(otherAddressInfoBean.getAddr4()) && addr5.equals(otherAddressInfoBean.getAddr5())
		        && addr6.equals(otherAddressInfoBean.getAddr6()) && addr7.equals(otherAddressInfoBean.getAddr7())
		        && addr8.equals(otherAddressInfoBean.getAddr8()) && addr9.equals(otherAddressInfoBean.getAddr9())
		        && addr10.equals(otherAddressInfoBean.getAddr10()) && addr11.equals(otherAddressInfoBean.getAddr11())
		        && addr12.equals(otherAddressInfoBean.getAddr12()) && addr13.equals(otherAddressInfoBean.getAddr13())
		        && addr14.equals(otherAddressInfoBean.getAddr14()))
		    return true;

		return false;
	}

	// UIM only look for SB (addr17), flat (addr1) and floor (addr3).
	public boolean isSameAddressForUim(AddressInfoBean otherAddressInfoBean)
	{
		if (otherAddressInfoBean == null) return false;

		if (UtilBase.isNotEmptyValue(addrId) && addrId.equals(otherAddressInfoBean.getAddrId()))
			return true;
		else if (addr1.equals(otherAddressInfoBean.getAddr1()) && addr3.equals(otherAddressInfoBean.getAddr3())
		        && addr17.equals(otherAddressInfoBean.getAddr17()))
		    return true;

		return false;
	}
}