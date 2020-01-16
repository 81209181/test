package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdTicketMasBean;
import com.hkt.btu.sd.facade.data.cloud.HktCloudViewData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;

public class HktCloudViewDataPopulator extends AbstractDataPopulator<SdTicketMasData> {

    public void populate(SdTicketMasBean source, HktCloudViewData target) {
        target.setRequestId(String.valueOf(source.getTicketMasId()));
        target.setStatus("");
        target.setStatusDesc(String.valueOf(source.getStatus()));
        target.setCreatedBy(source.getCreateby());
        target.setCreatedDate(source.getCreatedate().toString());
        target.setClosedDate(source.getCompleteDate() ==null? "":source.getCompleteDate().toString());
        target.setSubject("");
    }
}
