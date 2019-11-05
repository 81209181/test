package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketRemarkBean;
import com.hkt.btu.sd.facade.data.SdTicketRemarkData;
import com.hkt.btu.sd.facade.data.wfm.WfmJobProgressData;
import com.hkt.btu.sd.facade.data.wfm.WfmJobRemarksData;

public class SdTicketRemarkDataPopulator extends AbstractDataPopulator<SdTicketRemarkData> {

    public void populate(SdTicketRemarkBean source, SdTicketRemarkData target) {
        target.setTicketMasId(source.getTicketMasId());
        target.setRemarksType(source.getRemarksType());
        target.setRemarks(source.getRemarks());
        target.setCreatedate(source.getCreatedate());
        target.setCreateby(source.getCreateby());
    }

    public void populateJobProgressData(WfmJobProgressData source, SdTicketRemarkData target) {
        target.setRemarksType("WFM Job Progress");
        target.setRemarks(source.getRemark());
        target.setCreatedate(source.getCreateDate().toLocalDateTime());
        target.setCreateby(source.getCreateId());
    }

    public void populateJobRemarkData(WfmJobRemarksData source, SdTicketRemarkData target) {
        target.setRemarksType("WFM Job Remark");
        target.setRemarks(source.getRemark());
        target.setCreatedate(source.getCreateDate().toLocalDateTime());
        target.setCreateby(source.getCreateId());
    }
}
