package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

public class SdTicketTimePeriodSummaryEntity extends BaseEntity {
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
