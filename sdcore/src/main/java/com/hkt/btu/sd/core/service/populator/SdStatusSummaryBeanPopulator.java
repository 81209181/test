package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.StatusSummaryEntity;
import com.hkt.btu.sd.core.service.bean.SdStatusSummaryBean;
import com.hkt.btu.sd.core.service.constant.TicketStatusEnum;

public class SdStatusSummaryBeanPopulator extends AbstractBeanPopulator<SdStatusSummaryBean> {

    public void populate(StatusSummaryEntity source, SdStatusSummaryBean target) {
        target.setStatus(TicketStatusEnum.getEnum(source.getStatus()));
//        target.setJobCnt(source.getJobCnt()==null ? 0 : source.getJobCnt());
//        target.setQueryCnt(source.getQueryCnt()==null ? 0 : source.getQueryCnt());
    }

}
