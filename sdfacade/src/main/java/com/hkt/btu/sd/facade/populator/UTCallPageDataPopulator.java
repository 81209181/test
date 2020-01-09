package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.UTCallRequestBean;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.wfm.WfmPendingOrderData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UTCallPageDataPopulator extends AbstractDataPopulator<UTCallPageData> {
    public void populate(UTCallRequestBean source, UTCallPageData target) {
        target.setUtCallId(source.getUTCALLID());
        target.setBsnNum(source.getBSNNUM());
        target.setCode(source.getCODE());
        target.setMsg(source.getMSG());
        target.setServiceCode(source.getSERVICECODE());
        target.setSeq(source.getSEQ());
        target.setSeqType(source.getSEQTYPE());
        target.setLastCheckDate(source.getLASTCHECKDATE());
        target.setCreateDate(source.getCreatedate());
    }
}