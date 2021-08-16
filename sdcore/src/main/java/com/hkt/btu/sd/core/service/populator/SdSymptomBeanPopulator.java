package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;

public class SdSymptomBeanPopulator extends AbstractBeanPopulator<SdSymptomBean> {

    public void populate(SdSymptomEntity source, SdSymptomBean target) {
        target.setSymptomCode(source.getSymptomCode());
        target.setSymptomDescription(source.getSymptomDescription());
        target.setSymptomGroupCode(source.getSymptomGroupCode());
        target.setSymptomGroupName(source.getSymptomGroupName());
        target.setVoiceLineTest(source.getVoiceLineTest());
        target.setApptMode(source.getApptMode());
        target.setCreatedate(source.getCreatedate());
        target.setCreateby(source.getCreateby());
        target.setModifydate(source.getModifydate());
        target.setModifyby(source.getModifyby());
    }
}
