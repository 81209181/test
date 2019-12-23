package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdPublicHolidayEntity;
import com.hkt.btu.sd.core.service.bean.SdPublicHolidayBean;

public class SdPublicHolidayBeanPopulator extends AbstractBeanPopulator<SdPublicHolidayBean> {

    public void populate(SdPublicHolidayEntity source, SdPublicHolidayBean target) {
        target.setPublicHoliday(source.getPublicHoliday());
        target.setDescription(source.getDescription());
    }

    public void populate(SdPublicHolidayBean source, SdPublicHolidayEntity target) {
        target.setPublicHoliday(source.getPublicHoliday());
        target.setDescription(source.getDescription());
    }
}
