package com.hkt.btu.sd.facade.data.itsm;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class ItsmSearchProfileResponseData implements DataInterface {
    private List<ItsmProfileData> list;



    public List<ItsmProfileData> getList() {
        return list;
    }

    public void setList(List<ItsmProfileData> list) {
        this.list = list;
    }
}
