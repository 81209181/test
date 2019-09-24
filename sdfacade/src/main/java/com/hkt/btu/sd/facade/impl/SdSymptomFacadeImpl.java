package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import com.hkt.btu.sd.facade.populator.SdSymptomDataPopulator;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;


public class SdSymptomFacadeImpl implements SdSymptomFacade {

    @Resource(name = "sdSymptomService")
    SdSymptomService sdSymptomService;

    @Resource(name = "symptomDataPopulator")
    SdSymptomDataPopulator symptomDataPopulator;

    @Override
    public List<SdSymptomData> getSymptomGroupList() {
        List<SdSymptomBean> beanList = sdSymptomService.getSymptomGroupList();
        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        List<SdSymptomData> dataList = new LinkedList<>();
        for (SdSymptomBean bean : beanList) {
            SdSymptomData data = new SdSymptomData();
            symptomDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public boolean createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription) {
        return sdSymptomService.createSymptom(symptomCode,symptomGroupCode,symptomDescription);
    }
}
