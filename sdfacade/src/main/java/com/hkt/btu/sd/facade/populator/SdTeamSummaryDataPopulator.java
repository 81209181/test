package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.TeamSummaryBean;
import com.hkt.btu.sd.facade.data.StatusSummaryData;
import com.hkt.btu.sd.facade.data.TeamSummaryData;

import java.util.Optional;
import java.util.stream.Collectors;

public class SdTeamSummaryDataPopulator extends AbstractDataPopulator<TeamSummaryData> {

    public void populate(TeamSummaryBean source, TeamSummaryData target) {
        target.setQueryTotal(source.getQueryTotal());
        target.setJobTotal(source.getJobTotal());
        target.setSummaryData(source.getSummaryData().stream().map(summaryBean -> {
            StatusSummaryData bean = new StatusSummaryData();
            bean.setJobCnt(summaryBean.getJobCnt());
            bean.setQueryCnt(summaryBean.getQueryCnt());
            bean.setStatus(summaryBean.getStatus().getStatusCode());
            return bean;
        }).collect(Collectors.toList()));
    }
}
