package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.StatusSummaryEntity;
import com.hkt.btu.sd.core.service.bean.StatusSummaryBean;
import com.hkt.btu.sd.core.service.bean.TeamSummaryBean;

public class SdTeamSummaryBeanPopulator extends AbstractBeanPopulator<TeamSummaryBean> {

    public void populateStatusSummaryBean(StatusSummaryEntity source, StatusSummaryBean target) {
        switch (source.getStatus()) {
            case "O":
                target.setStatus("Open");
                break;
            case "W":
                target.setStatus("Work");
                break;
            case "CP":
                target.setStatus("Complete");
                break;
        }
        target.setJobCnt(source.getJobCnt());
        target.setQueryCnt(source.getQueryCnt());
    }

    public void populate(StatusSummaryEntity source, TeamSummaryBean target) {
        target.setJobTotal(source.getJobCnt());
        target.setQueryTotal(source.getQueryCnt());
    }

}
