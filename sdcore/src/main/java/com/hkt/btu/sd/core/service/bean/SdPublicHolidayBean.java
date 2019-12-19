package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

import java.time.LocalDate;

public class SdPublicHolidayBean extends BaseBean {

    private LocalDate publicHoliday;
    private String description;

    public LocalDate getPublicHoliday() {
        return publicHoliday;
    }

    public void setPublicHoliday(LocalDate publicHoliday) {
        this.publicHoliday = publicHoliday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
