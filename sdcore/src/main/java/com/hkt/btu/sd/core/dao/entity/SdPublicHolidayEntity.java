package com.hkt.btu.sd.core.dao.entity;

import com.hkt.btu.common.core.dao.entity.BaseEntity;

import java.time.LocalDate;

public class SdPublicHolidayEntity extends BaseEntity {

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
