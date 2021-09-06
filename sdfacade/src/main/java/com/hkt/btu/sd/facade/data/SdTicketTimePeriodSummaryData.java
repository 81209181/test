package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdTicketTimePeriodSummaryData implements DataInterface {
    private String timePeriod;
    private int count;

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
