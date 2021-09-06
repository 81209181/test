package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketTimePeriodSummaryEntity;
import com.hkt.btu.sd.core.service.bean.SdOutstandingFaultBean;
import com.hkt.btu.sd.core.service.bean.SdTicketTimePeriodSummaryBean;

public class SdTicketTimePeriodSummaryBeanPopulator extends AbstractBeanPopulator<SdOutstandingFaultBean> {
    public void populate(SdTicketTimePeriodSummaryEntity source, SdTicketTimePeriodSummaryBean target) {
        switch (source.getTimePeriod()) {
            case "WITHIN_15":
                target.setTimePeriod("0-15");
                break;
            case "BETWEEN_15_30":
                target.setTimePeriod("15-30");
                break;
            case "BETWEEN_30_60":
                target.setTimePeriod("30-60");
                break;
            case "BETWEEN_1_2":
                target.setTimePeriod("60-120");
                break;
            case "BETWEEN_2_4":
                target.setTimePeriod("120-240");
                break;
            case "BETWEEN_4_8":
                target.setTimePeriod("240-480");
                break;
            case "OVER_8":
                target.setTimePeriod("over_480");
                break;
        }
        target.setCount(source.getCount());
    }
}
