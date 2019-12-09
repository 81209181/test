package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;
import com.hkt.btu.sd.facade.constant.DnPlanActionEnum;

public class SdDnPlanData implements DataInterface {
    private String param;
    private String plan;
    private DnPlanActionEnum action;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public DnPlanActionEnum getAction() {
        return action;
    }

    public void setAction(DnPlanActionEnum action) {
        this.action = action;
    }
}
