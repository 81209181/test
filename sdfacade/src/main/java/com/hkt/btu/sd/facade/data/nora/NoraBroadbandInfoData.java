package com.hkt.btu.sd.facade.data.nora;


import com.hkt.btu.common.facade.data.DataInterface;

import java.util.Date;

public class NoraBroadbandInfoData implements DataInterface {
	
	private long broadbandSeqId = 0;
	
	private String broadbandStatus = "";
	
	private String companyId = "";

	private String groupId = "";
	
	private String identityId = "";
	
	private int subnetSize = 0;
	private String gatewayIp = "";
	private String subnetMask = "";
	private String phoneStartIp = "";
	private String needReassign = "";
	
	private long itemId = 0L;
	
	private String itemCode = "";
	
	private String itemName = "";
	
	private long offerId = 0L;
	
	private String offerName = "";
	
	private String bandwidth = "";
	
	private String classType = "";
	
	private String customerWan = "";
	
	private String deploymentMethod = "";
	
	private String domain = "";
	
	private String equipment1 = "";
	
	private String equipment2 = "";
	
	private String fixedIp = "";
	
	private String hktWan = "";
	
	private String ipMode = "";
	
	private String internetAccess = "";
	
	private String needInstallationAddress = "";
	
	private int fixIpNo = 0;
	
	private String oldFsa = "";
	
	private String pppoeLoginID = "";

	private String productType = "";
	
	private int relativeDuration = 0;
	
	private int slaPeriod = 0;
	
	private String signalFlag = "";
	
	private String tempBsn = "";
	
	private String tempService = "";
	
	private String transmissionMode = "";
	
	private String trueIp = "";

	private Date lastUpdatedDate;
	private String lastUpdatedBy = "";
	
	private String actionType = "";
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getGatewayIp() {
		return gatewayIp;
	}
	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}
	public String getSubnetMask() {
		return subnetMask;
	}
	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}
	public String getPhoneStartIp() {
		return phoneStartIp;
	}
	public void setPhoneStartIp(String phoneStartIp) {
		this.phoneStartIp = phoneStartIp;
	}
	public long getBroadbandSeqId() {
		return broadbandSeqId;
	}
	public void setBroadbandSeqId(long broadbandSeqId) {
		this.broadbandSeqId = broadbandSeqId;
	}
	public String getBroadbandStatus() {
		return broadbandStatus;
	}
	public void setBroadbandStatus(String broadbandStatus) {
		this.broadbandStatus = broadbandStatus;
	}
	public int getSubnetSize() {
		return subnetSize;
	}
	public void setSubnetSize(int subnetSize) {
		this.subnetSize = subnetSize;
	}
	public String getNeedReassign() {
		return needReassign;
	}
	public void setNeedReassign(String needReassign) {
		this.needReassign = needReassign;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public long getOfferId() {
		return offerId;
	}
	public void setOfferId(long offerId) {
		this.offerId = offerId;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getCustomerWan() {
		return customerWan;
	}
	public void setCustomerWan(String customerWan) {
		this.customerWan = customerWan;
	}
	public String getDeploymentMethod() {
		return deploymentMethod;
	}
	public void setDeploymentMethod(String deploymentMethod) {
		this.deploymentMethod = deploymentMethod;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getEquipment1() {
		return equipment1;
	}
	public void setEquipment1(String equipment1) {
		this.equipment1 = equipment1;
	}
	public String getEquipment2() {
		return equipment2;
	}
	public void setEquipment2(String equipment2) {
		this.equipment2 = equipment2;
	}
	public String getFixedIp() {
		return fixedIp;
	}
	public void setFixedIp(String fixedIp) {
		this.fixedIp = fixedIp;
	}
	public String getHktWan() {
		return hktWan;
	}
	public void setHktWan(String hktWan) {
		this.hktWan = hktWan;
	}
	public String getIpMode() {
		return ipMode;
	}
	public void setIpMode(String ipMode) {
		this.ipMode = ipMode;
	}
	public String getInternetAccess() {
		return internetAccess;
	}
	public void setInternetAccess(String internetAccess) {
		this.internetAccess = internetAccess;
	}
	public String getNeedInstallationAddress() {
		return needInstallationAddress;
	}
	public void setNeedInstallationAddress(String needInstallationAddress) {
		this.needInstallationAddress = needInstallationAddress;
	}
	public int getFixIpNo() {
		return fixIpNo;
	}
	public void setFixIpNo(int fixIpNo) {
		this.fixIpNo = fixIpNo;
	}
	public String getOldFsa() {
		return oldFsa;
	}
	public void setOldFsa(String oldFsa) {
		this.oldFsa = oldFsa;
	}
	public String getPppoeLoginID() {
		return pppoeLoginID;
	}
	public void setPppoeLoginID(String pppoeLoginID) {
		this.pppoeLoginID = pppoeLoginID;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public int getRelativeDuration() {
		return relativeDuration;
	}
	public void setRelativeDuration(int relativeDuration) {
		this.relativeDuration = relativeDuration;
	}
	public int getSlaPeriod() {
		return slaPeriod;
	}
	public void setSlaPeriod(int slaPeriod) {
		this.slaPeriod = slaPeriod;
	}
	public String getSignalFlag() {
		return signalFlag;
	}
	public void setSignalFlag(String signalFlag) {
		this.signalFlag = signalFlag;
	}
	public String getTempBsn() {
		return tempBsn;
	}
	public void setTempBsn(String tempBsn) {
		this.tempBsn = tempBsn;
	}
	public String getTempService() {
		return tempService;
	}
	public void setTempService(String tempService) {
		this.tempService = tempService;
	}
	public String getTransmissionMode() {
		return transmissionMode;
	}
	public void setTransmissionMode(String transmissionMode) {
		this.transmissionMode = transmissionMode;
	}
	public String getTrueIp() {
		return trueIp;
	}
	public void setTrueIp(String trueIp) {
		this.trueIp = trueIp;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	
}
