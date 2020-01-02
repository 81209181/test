package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.StatusSummaryEntity;
import com.hkt.btu.sd.core.service.bean.TeamSummaryBean;

public class SdTeamSummaryBeanPopulator extends AbstractBeanPopulator<TeamSummaryBean> {

    public void populate(StatusSummaryEntity source, TeamSummaryBean target) {
        switch (source.getStatus()) {
            case "O":
                target.setOpenQueryCount(source.getQueryCnt());
                target.setOpenJobCount(source.getJobCnt());
                break;
            case "W":
                target.setWorkQueryCount(source.getQueryCnt());
                target.setWorkJobCount(source.getJobCnt());
                break;
            case "CP":
                target.setCompleteQueryCount(source.getQueryCnt());
                target.setCompleteJobCount(source.getJobCnt());
                break;
            default:
                target.setQueryTotal(source.getQueryCnt());
                target.setJobTotal(source.getJobCnt());
        }
    }

}
