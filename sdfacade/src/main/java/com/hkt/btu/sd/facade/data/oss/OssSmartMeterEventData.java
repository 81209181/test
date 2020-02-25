package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.common.facade.data.DataInterface;

public class OssSmartMeterEventData implements DataInterface {
    private Integer eventId;
    private String eventCode;
    private String eventDesc;
    private String eventTime;


    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
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
