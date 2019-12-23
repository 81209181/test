package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdPublicHolidayBean;
import com.hkt.btu.sd.facade.data.SdPublicHolidayData;

public class SdPublicHolidayDataPopulator extends AbstractDataPopulator<SdPublicHolidayData> {

    public void populate(SdPublicHolidayBean source, SdPublicHolidayData target) {
        target.setPublicHoliday(source.getPublicHoliday());
        target.setDescription(source.getDescription());
    }

    public void populate(SdPublicHolidayData source, SdPublicHolidayBean target) {
        target.setPublicHoliday(source.getPublicHoliday());
        target.setDescription(source.getDescription());
    }
}
