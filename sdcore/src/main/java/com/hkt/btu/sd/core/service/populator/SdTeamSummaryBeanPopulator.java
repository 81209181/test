package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.StatusSummaryEntity;
import com.hkt.btu.sd.core.service.bean.TeamSummaryBean;

public class SdTeamSummaryBeanPopulator extends AbstractBeanPopulator<TeamSummaryBean> {

    public void populate(StatusSummaryEntity source, TeamSummaryBean target) {
        target.setJobTotal(source.getJobCnt()==null ? 0 : source.getJobCnt());
        target.setQueryTotal(source.getQueryCnt()==null ? 0 : source.getQueryCnt());
    }

}
