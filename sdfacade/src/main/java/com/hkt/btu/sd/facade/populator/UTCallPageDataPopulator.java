package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.UTCallPageBean;
import com.hkt.btu.sd.facade.data.*;

public class UTCallPageDataPopulator extends AbstractDataPopulator<SdUtCallPageData> {
    public void populate(UTCallPageBean source, SdUtCallPageData target) {
        target.setUtCallId(source.getUtCallId());
        target.setBsnNum(source.getBsnNum());
        target.setCode(source.getCode());
        target.setMsg(source.getMsg());
        target.setServiceCode(source.getServiceCode());
        target.setSeq(source.getSeq());
        target.setSeqType(source.getSeqType());
        target.setTestStatus(source.getTestStatus());
        target.setLastCheckDate(source.getLastCheckDate());
        target.setTicketDetId(source.getTicketDetId());
        target.setCreateDate(source.getCreatedate());
    }
}