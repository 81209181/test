package com.hkt.btu.noc.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class NocAccessRequestLocData implements DataInterface {
    private String locId;
    private String name;


    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
