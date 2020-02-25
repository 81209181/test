package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.common.facade.data.DataInterface;
import com.hkt.btu.common.facade.data.PageData;

public class OssSmartMeterEventWrapData implements DataInterface {

    public OssSmartMeterEventWrapData() {
        events = new PageData<>();
    }

    private PageData<OssSmartMeterEventData> events;

    public PageData<OssSmartMeterEventData> getEvents() {
        return events;
    }

    public void setEvents(PageData<OssSmartMeterEventData> events) {
        this.events = events;
    }
}
