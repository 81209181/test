package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.UTCallPageEntity;
import com.hkt.btu.sd.core.service.bean.UTCallPageBean;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class UTCallPageBeanPopulator extends AbstractBeanPopulator<UTCallPageBean> {
    public void populate(UTCallPageEntity source, UTCallPageBean target) {
        super.populate(source, target);

        target.setUtCallId(source.getUtCallId());
        target.setBsnNum(source.getBsnNum());
        target.setCode(source.getCode());
        target.setMsg(source.getMsg());
        target.setServiceCode(source.getServiceCode());
        target.setSeq(source.getSeq());
        target.setSeqType(source.getSeqType());
        target.setTestStatus(source.getTestStatus());
        target.setLastCheckDate(source.getLastCheckDate());
    }
}