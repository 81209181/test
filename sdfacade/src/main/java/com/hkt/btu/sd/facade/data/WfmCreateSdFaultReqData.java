package com.hkt.btu.sd.facade.data;

public class WfmCreateSdFaultReqData {
    private String staffId;
    private WfmRequestDetailsBeanDate requestDetailsBean;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public WfmRequestDetailsBeanDate getRequestDetailsBean() {
        return requestDetailsBean;
    }

    public void setRequestDetailsBean(WfmRequestDetailsBeanDate requestDetailsBean) {
        this.requestDetailsBean = requestDetailsBean;
    }
}
