package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdServiceFaultsData implements DataInterface {

    private String faults;

    public String getFaults() {
        return faults;
    }

    public void setFaults(String faults) {
        this.faults = faults;
    }
}
