package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import com.hkt.btu.sd.facade.populator.SdServiceTypeDataPopulator;
import com.hkt.btu.sd.facade.populator.SdSymptomDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;


public class SdSymptomFacadeImpl implements SdSymptomFacade {

    @Resource(name = "sdSymptomService")
    SdSymptomService sdSymptomService;

    @Resource(name = "symptomDataPopulator")
    SdSymptomDataPopulator symptomDataPopulator;

    @Resource(name = "serviceTypeDataPopulator")
    SdServiceTypeDataPopulator serviceTypeDataPopulator;

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
    public String createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription) {
        if (StringUtils.isEmpty(symptomCode)) {
            return "Symptom Code empty.";
        }
        if (StringUtils.isEmpty(symptomGroupCode)) {
            return "Symptom Group Code empty.";
        }
        if (StringUtils.isEmpty(symptomDescription)) {
            return "Symptom Description empty.";
        }
        if (sdSymptomService.checkSymptom(symptomCode)) {
            return "Symptom Code already exists.";
        }

        sdSymptomService.createSymptom(symptomCode,symptomGroupCode,symptomDescription);
        return null;
    }

    @Override
    public List<SdServiceTypeData> getServiceTypeList() {
        List<SdServiceTypeBean> beanList = sdSymptomService.getServiceTypeList();
        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        List<SdServiceTypeData> dataList = new LinkedList<>();
        for (SdServiceTypeBean bean : beanList) {
            SdServiceTypeData data = new SdServiceTypeData();
            serviceTypeDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public String createSymptomMapping(String serviceTypeCode, String symptomGroupCode) {
        if (StringUtils.isEmpty(serviceTypeCode)) {
            return "Service Type Code empty.";
        }
        if (StringUtils.isEmpty(symptomGroupCode)) {
            return "Symptom Group Code empty.";
        }
        if (sdSymptomService.checkSymptomMapping(serviceTypeCode, symptomGroupCode)) {
            return "Service Type, Symptom Group already exists.";
        }

        sdSymptomService.createSymptomMapping(serviceTypeCode,symptomGroupCode);
        return null;
    }

    @Override
    public String editSymptomMapping(String oldServiceTypeCode, String oldSymptomGroupCode, String serviceTypeCode, String symptomGroupCode) {
        if (oldServiceTypeCode.equals(serviceTypeCode) && oldSymptomGroupCode.equals(symptomGroupCode)) {
            /**/
        } else {
            if (sdSymptomService.checkSymptomMapping(serviceTypeCode, symptomGroupCode)) {
                return "Service Type, Symptom Group already exists.";
            }
        }

        sdSymptomService.deleteSymptomMapping(oldServiceTypeCode,oldSymptomGroupCode);
        sdSymptomService.createSymptomMapping(serviceTypeCode,symptomGroupCode);
        return null;
    }
}
