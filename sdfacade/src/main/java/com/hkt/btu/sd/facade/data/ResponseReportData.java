package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class ResponseReportData implements DataInterface {

    private String reportId;
    private String errorMsg;

    public static ResponseReportData of(String reportId, String errorMsg) {
        return new ResponseReportData(reportId, errorMsg);
    }

    private ResponseReportData(String reportId, String errorMsg) {
        this.reportId = reportId;
        this.errorMsg = errorMsg;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
