package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class WfmJobBeanData implements DataInterface {
    private Integer jobId;
    private String assignArea;
    private String deptId;
    private String sysId;
    private String marketSeg;
    private String isAutoAssign;
    private String status;
    private Integer estJobDur;
    private String jobType;
    private String assignTech;
    private String helper1;
    private String helper2;
    private Integer priority;
    private String receiveDate;
    private String actionDate;
    private String srDate;
    private String apptDate;
    private String apptDateString;
    private String apptSTime;
    private String apptSTimeString;
    private String apptETime;
    private String apptETimeString;
    private Integer apptTicket = 0;

    private Integer bundleId = 0;
    private String bundleInd;
    private String bundleApptTicket;

    private String cancelInd;
    private String fieldInd;

    private String preWiringStartDate;
    private String preWiringEndDate;
    private String preSiteCheckStartDate;
    private String preSiteCheckEndDate;

    // Order Info
    private String orderNum;
    private String faultId;
    private String serviceNum;
    private String srvType;
    private String prodType;
    private String workType;
    private String orderType;
    private Integer lineNo = 1;
    private String addressCode;
    private String exchId;
    private String rlu;
    private String l1OrderNum;
    private String fsa;
    private String chargeCd;
    private String fiberReady;
    private String sb;
    private String buildingId;
    private String ibiWorkInd;
    private String orderStatus;

    // Cust Info
    private String custAcct;
    private String custEmail;
    private String custId;
    private String custRequest;
    private String custContact;
    private String custContact2;
    private String custType;
    private String custName;
    private String flat;
    private String floor;
    private String hseLotNo;
    private String building;
    private String streetNo;
    private String streetName;
    private String streetCat;
    private String section;
    private String district;
    private String area;
    private String blockageCode;
    private String returnRemark;
    private Boolean fileAttachedInd;
    private String overallProgress;

    private Integer parentJobId;
    private List<String> techIds;
    private String relation;

    private String apptId;

    private String location;
    private List<String> skills;
    @SuppressWarnings("rawtypes")
    private List previousStartTimes;
    private Integer starttime1;
    private Integer startTime2;
    private Boolean isAutoAssignInAA;
    private String assignedStaff;
    private Integer arriveTime;
    private String createId;

    private String besOrderNum;
    private String tenantId;
    private String service;
    private String mainOffer;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getAssignArea() {
        return assignArea;
    }

    public void setAssignArea(String assignArea) {
        this.assignArea = assignArea;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getMarketSeg() {
        return marketSeg;
    }

    public void setMarketSeg(String marketSeg) {
        this.marketSeg = marketSeg;
    }

    public String getIsAutoAssign() {
        return isAutoAssign;
    }

    public void setIsAutoAssign(String isAutoAssign) {
        this.isAutoAssign = isAutoAssign;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getEstJobDur() {
        return estJobDur;
    }

    public void setEstJobDur(Integer estJobDur) {
        this.estJobDur = estJobDur;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getAssignTech() {
        return assignTech;
    }

    public void setAssignTech(String assignTech) {
        this.assignTech = assignTech;
    }

    public String getHelper1() {
        return helper1;
    }

    public void setHelper1(String helper1) {
        this.helper1 = helper1;
    }

    public String getHelper2() {
        return helper2;
    }

    public void setHelper2(String helper2) {
        this.helper2 = helper2;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getSrDate() {
        return srDate;
    }

    public void setSrDate(String srDate) {
        this.srDate = srDate;
    }

    public String getApptDate() {
        return apptDate;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public String getApptDateString() {
        return apptDateString;
    }

    public void setApptDateString(String apptDateString) {
        this.apptDateString = apptDateString;
    }

    public String getApptSTime() {
        return apptSTime;
    }

    public void setApptSTime(String apptSTime) {
        this.apptSTime = apptSTime;
    }

    public String getApptSTimeString() {
        return apptSTimeString;
    }

    public void setApptSTimeString(String apptSTimeString) {
        this.apptSTimeString = apptSTimeString;
    }

    public String getApptETime() {
        return apptETime;
    }

    public void setApptETime(String apptETime) {
        this.apptETime = apptETime;
    }

    public String getApptETimeString() {
        return apptETimeString;
    }

    public void setApptETimeString(String apptETimeString) {
        this.apptETimeString = apptETimeString;
    }

    public Integer getApptTicket() {
        return apptTicket;
    }

    public void setApptTicket(Integer apptTicket) {
        this.apptTicket = apptTicket;
    }

    public Integer getBundleId() {
        return bundleId;
    }

    public void setBundleId(Integer bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleInd() {
        return bundleInd;
    }

    public void setBundleInd(String bundleInd) {
        this.bundleInd = bundleInd;
    }

    public String getBundleApptTicket() {
        return bundleApptTicket;
    }

    public void setBundleApptTicket(String bundleApptTicket) {
        this.bundleApptTicket = bundleApptTicket;
    }

    public String getCancelInd() {
        return cancelInd;
    }

    public void setCancelInd(String cancelInd) {
        this.cancelInd = cancelInd;
    }

    public String getFieldInd() {
        return fieldInd;
    }

    public void setFieldInd(String fieldInd) {
        this.fieldInd = fieldInd;
    }

    public String getPreWiringStartDate() {
        return preWiringStartDate;
    }

    public void setPreWiringStartDate(String preWiringStartDate) {
        this.preWiringStartDate = preWiringStartDate;
    }

    public String getPreWiringEndDate() {
        return preWiringEndDate;
    }

    public void setPreWiringEndDate(String preWiringEndDate) {
        this.preWiringEndDate = preWiringEndDate;
    }

    public String getPreSiteCheckStartDate() {
        return preSiteCheckStartDate;
    }

    public void setPreSiteCheckStartDate(String preSiteCheckStartDate) {
        this.preSiteCheckStartDate = preSiteCheckStartDate;
    }

    public String getPreSiteCheckEndDate() {
        return preSiteCheckEndDate;
    }

    public void setPreSiteCheckEndDate(String preSiteCheckEndDate) {
        this.preSiteCheckEndDate = preSiteCheckEndDate;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
    }

    public String getServiceNum() {
        return serviceNum;
    }

    public void setServiceNum(String serviceNum) {
        this.serviceNum = serviceNum;
    }

    public String getSrvType() {
        return srvType;
    }

    public void setSrvType(String srvType) {
        this.srvType = srvType;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getExchId() {
        return exchId;
    }

    public void setExchId(String exchId) {
        this.exchId = exchId;
    }

    public String getRlu() {
        return rlu;
    }

    public void setRlu(String rlu) {
        this.rlu = rlu;
    }

    public String getL1OrderNum() {
        return l1OrderNum;
    }

    public void setL1OrderNum(String l1OrderNum) {
        this.l1OrderNum = l1OrderNum;
    }

    public String getFsa() {
        return fsa;
    }

    public void setFsa(String fsa) {
        this.fsa = fsa;
    }

    public String getChargeCd() {
        return chargeCd;
    }

    public void setChargeCd(String chargeCd) {
        this.chargeCd = chargeCd;
    }

    public String getFiberReady() {
        return fiberReady;
    }

    public void setFiberReady(String fiberReady) {
        this.fiberReady = fiberReady;
    }

    public String getSb() {
        return sb;
    }

    public void setSb(String sb) {
        this.sb = sb;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getIbiWorkInd() {
        return ibiWorkInd;
    }

    public void setIbiWorkInd(String ibiWorkInd) {
        this.ibiWorkInd = ibiWorkInd;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustAcct() {
        return custAcct;
    }

    public void setCustAcct(String custAcct) {
        this.custAcct = custAcct;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustRequest() {
        return custRequest;
    }

    public void setCustRequest(String custRequest) {
        this.custRequest = custRequest;
    }

    public String getCustContact() {
        return custContact;
    }

    public void setCustContact(String custContact) {
        this.custContact = custContact;
    }

    public String getCustContact2() {
        return custContact2;
    }

    public void setCustContact2(String custContact2) {
        this.custContact2 = custContact2;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getHseLotNo() {
        return hseLotNo;
    }

    public void setHseLotNo(String hseLotNo) {
        this.hseLotNo = hseLotNo;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetCat() {
        return streetCat;
    }

    public void setStreetCat(String streetCat) {
        this.streetCat = streetCat;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBlockageCode() {
        return blockageCode;
    }

    public void setBlockageCode(String blockageCode) {
        this.blockageCode = blockageCode;
    }

    public String getReturnRemark() {
        return returnRemark;
    }

    public void setReturnRemark(String returnRemark) {
        this.returnRemark = returnRemark;
    }

    public Boolean getFileAttachedInd() {
        return fileAttachedInd;
    }

    public void setFileAttachedInd(Boolean fileAttachedInd) {
        this.fileAttachedInd = fileAttachedInd;
    }

    public String getOverallProgress() {
        return overallProgress;
    }

    public void setOverallProgress(String overallProgress) {
        this.overallProgress = overallProgress;
    }

    public Integer getParentJobId() {
        return parentJobId;
    }

    public void setParentJobId(Integer parentJobId) {
        this.parentJobId = parentJobId;
    }

    public List<String> getTechIds() {
        return techIds;
    }

    public void setTechIds(List<String> techIds) {
        this.techIds = techIds;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getApptId() {
        return apptId;
    }

    public void setApptId(String apptId) {
        this.apptId = apptId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List getPreviousStartTimes() {
        return previousStartTimes;
    }

    public void setPreviousStartTimes(List previousStartTimes) {
        this.previousStartTimes = previousStartTimes;
    }

    public Integer getStarttime1() {
        return starttime1;
    }

    public void setStarttime1(Integer starttime1) {
        this.starttime1 = starttime1;
    }

    public Integer getStartTime2() {
        return startTime2;
    }

    public void setStartTime2(Integer startTime2) {
        this.startTime2 = startTime2;
    }

    public Boolean getAutoAssignInAA() {
        return isAutoAssignInAA;
    }

    public void setAutoAssignInAA(Boolean autoAssignInAA) {
        isAutoAssignInAA = autoAssignInAA;
    }

    public String getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(String assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public Integer getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Integer arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getBesOrderNum() {
        return besOrderNum;
    }

    public void setBesOrderNum(String besOrderNum) {
        this.besOrderNum = besOrderNum;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMainOffer() {
        return mainOffer;
    }

    public void setMainOffer(String mainOffer) {
        this.mainOffer = mainOffer;
    }
}
