package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.TeamSummaryBean;
import com.hkt.btu.sd.facade.data.TeamSummaryData;

public class SdTeamSummaryDataPopulator extends AbstractDataPopulator<TeamSummaryData> {

    public void populate(TeamSummaryBean source, TeamSummaryData target) {
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
