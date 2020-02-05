package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.UTCallRequestEntity;
import com.hkt.btu.sd.core.service.bean.SdUtCallRequestBean;

public class UTCallRequestBeanPopulator extends AbstractBeanPopulator<SdUtCallRequestBean> {
    public void populate(UTCallRequestEntity source, SdUtCallRequestBean target) {
        super.populate(source, target);

        target.setUtCallId(source.getUtCallId());
        target.setBsnNum(source.getBsnNum());
        target.setCode(source.getCode());
        target.setMsg(source.getMsg());
        target.setServiceCode(source.getServiceCode());
        target.setSeq(source.getSeq());
        target.setSeqType(source.getSeqType());
        target.setLastCheckDate(source.getLastCheckDate());
        target.setTicketDetId(source.getTicketDetId());
    }
}