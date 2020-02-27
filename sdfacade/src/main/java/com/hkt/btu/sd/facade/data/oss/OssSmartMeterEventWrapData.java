package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.common.facade.data.BtuPageData;
import com.hkt.btu.common.facade.data.DataInterface;

public class OssSmartMeterEventWrapData implements DataInterface {

    public OssSmartMeterEventWrapData() {
        events = new BtuPageData<>();
    }

    private BtuPageData<OssSmartMeterEventData> events;

    public BtuPageData<OssSmartMeterEventData> getEvents() {
        return events;
    }

    public void setEvents(BtuPageData<OssSmartMeterEventData> events) {
        this.events = events;
    }
}
