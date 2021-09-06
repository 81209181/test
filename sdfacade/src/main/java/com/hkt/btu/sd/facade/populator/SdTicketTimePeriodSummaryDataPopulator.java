package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketTimePeriodSummaryBean;
import com.hkt.btu.sd.facade.data.SdTicketTimePeriodSummaryData;

public class SdTicketTimePeriodSummaryDataPopulator extends AbstractDataPopulator<SdTicketTimePeriodSummaryData> {
    public void populate(SdTicketTimePeriodSummaryBean source, SdTicketTimePeriodSummaryData target) {
        target.setTimePeriod(source.getTimePeriod());
        target.setCount(source.getCount());
    }
}
