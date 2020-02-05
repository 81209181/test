package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTeamSummaryBean;
import com.hkt.btu.sd.facade.data.SdTeamSummaryData;

public class SdTeamSummaryDataPopulator extends AbstractDataPopulator<SdTeamSummaryData> {

    public void populate(SdTeamSummaryBean source, SdTeamSummaryData target) {
        target.setQueryTotal(source.getQueryTotal());
        target.setJobTotal(source.getJobTotal());
        target.setOpenQueryCount(source.getOpenQueryCount());
        target.setOpenJobCount(source.getOpenJobCount());
        target.setWorkQueryCount(source.getWorkQueryCount());
        target.setWorkJobCount(source.getWorkJobCount());
        target.setCompleteJobCount(source.getCompleteJobCount());
        target.setCompleteQueryCount(source.getCompleteQueryCount());
    }
}
