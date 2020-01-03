package com.hkt.btu.sd.facade.data;


import com.hkt.btu.common.facade.data.DataInterface;

import java.time.LocalDate;
import java.util.Objects;


public class SdPublicHolidayData implements DataInterface {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SdPublicHolidayData that = (SdPublicHolidayData) o;
        return Objects.equals(publicHoliday, that.publicHoliday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicHoliday);
    }
}
