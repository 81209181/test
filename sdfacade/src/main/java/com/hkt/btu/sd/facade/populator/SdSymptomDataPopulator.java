package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.facade.data.SdConfigParamData;
import com.hkt.btu.sd.facade.data.SdSymptomData;

public class SdSymptomDataPopulator extends AbstractDataPopulator<SdSymptomData> {

    public void populate(SdSymptomBean source, SdSymptomData target) {
        target.setSymptomCode(source.getSymptomCode());
        target.setSymptomDescription(source.getSymptomDescription());
        target.setSymptomGroupCode(source.getSymptomGroupCode());
        target.setSymptomGroupName(source.getSymptomGroupName());
        target.setCreatedate(source.getCreatedate());
        target.setCreateby(source.getCreateby());
        target.setModifydate(source.getModifydate());
        target.setModifyby(source.getModifyby());
    }
}
