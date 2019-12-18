package com.hkt.btu.sd.facade.data;


import com.hkt.btu.common.facade.data.DataInterface;

import java.time.LocalDate;


public class SdPublicHolidayData implements DataInterface {

    private LocalDate PublicHoliday;
    private String description;

    public LocalDate getPublicHoliday() {
        return PublicHoliday;
    }

    public void setPublicHoliday(LocalDate publicHoliday) {
        PublicHoliday = publicHoliday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
