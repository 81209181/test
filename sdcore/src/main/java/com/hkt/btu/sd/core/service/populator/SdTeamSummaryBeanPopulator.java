package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdTicketMasEntity;
import com.hkt.btu.sd.core.dao.entity.StatusSummaryEntity;
import com.hkt.btu.sd.core.service.bean.StatusSummaryBean;
import com.hkt.btu.sd.core.service.bean.TeamSummaryBean;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;

public class SdTeamSummaryBeanPopulator extends AbstractBeanPopulator<TeamSummaryBean> {

    public void populateStatusSummaryBean(StatusSummaryEntity source, StatusSummaryBean target) {
        target.setStatus(source.getStatus()); // todo SERVDESK-210:  use constant, use TicketStatusEnum, ref com/hkt/btu/sd/core/service/populator/SdTicketMasBeanPopulator.java
        target.setJobCnt(source.getJobCnt());
        target.setQueryCnt(source.getQueryCnt());
    }

    public void populate(StatusSummaryEntity source, TeamSummaryBean target) {
        target.setJobTotal(source.getJobCnt());
        target.setQueryTotal(source.getQueryCnt());
    }

}
