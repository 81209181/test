package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdServiceAddressData implements DataInterface {

    private String serviceAddress;
    private String gridId;
    private String exchangeBuildingId;

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getExchangeBuildingId() {
        return exchangeBuildingId;
    }

    public void setExchangeBuildingId(String exchangeBuildingId) {
        this.exchangeBuildingId = exchangeBuildingId;
    }
}
