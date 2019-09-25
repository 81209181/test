package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class WfmJobDetailsData implements DataInterface {
    private WfmFaultBeanData faultBean;
    private WfmJobBeanData jobBean;
    private WfmJobCompleteBeanData jobCompleteBean;

    public WfmFaultBeanData getFaultBean() {
        return faultBean;
    }

    public void setFaultBean(WfmFaultBeanData faultBean) {
        this.faultBean = faultBean;
    }

    public WfmJobBeanData getJobBean() {
        return jobBean;
    }

    public void setJobBean(WfmJobBeanData jobBean) {
        this.jobBean = jobBean;
    }

    public WfmJobCompleteBeanData getJobCompleteBean() {
        return jobCompleteBean;
    }

    public void setJobCompleteBean(WfmJobCompleteBeanData jobCompleteBean) {
        this.jobCompleteBean = jobCompleteBean;
    }
}
