package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.common.facade.data.DataInterface;

public class OssSmartMeterEventData implements DataInterface {
    private Integer eventID;
    private String hwUnitName;
    private String eventDesc;
    private String eventTime;

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public String getHwUnitName() {
        return hwUnitName;
    }

    public void setHwUnitName(String hwUnitName) {
        this.hwUnitName = hwUnitName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
}
