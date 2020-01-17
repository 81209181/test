package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.UTCallPageBean;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.wfm.WfmPendingOrderData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UTCallPageDataPopulator extends AbstractDataPopulator<UTCallPageData> {
    public void populate(UTCallPageBean source, UTCallPageData target) {
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