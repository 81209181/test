package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.populator.SdSymptomDataPopulator;
import com.hkt.btu.sd.facade.populator.SdSymptomMappingDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class SdSymptomFacadeImpl implements SdSymptomFacade {
    private static final Logger LOG = LogManager.getLogger(SdSymptomFacadeImpl.class);

    @Resource(name = "sdSymptomService")
    SdSymptomService sdSymptomService;
    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;

    @Resource(name = "symptomDataPopulator")
    SdSymptomDataPopulator symptomDataPopulator;

    @Resource(name = "symptomMappingDataPopulator")
    SdSymptomMappingDataPopulator symptomMappingDataPopulator;

    @Override
    public List<SdSymptomData> getSymptomGroupList() {
        List<SdSymptomBean> beanList = sdSymptomService.getSymptomGroupList();
        return buildSymptomDataList(beanList);
    }

    @Override
    public String createSymptom(String symptomCode, String symptomGroupCode, String symptomDescription) {
        if (StringUtils.isEmpty(symptomCode)) {
            return "Empty Symptom Code.";
        } else if (StringUtils.isEmpty(symptomGroupCode)) {
            return "Empty Symptom Group Code.";
        } else if (StringUtils.isEmpty(symptomDescription)) {
            return "Empty Symptom Description.";
        }

        try {
            sdSymptomService.createSymptom(symptomCode, symptomGroupCode, symptomDescription);
        } catch (DuplicateKeyException e){
            return "Symptom Code already exists.";
        }

        return null;
    }

    @Override
    public PageData<SdSymptomData> searchSymptomList(Pageable pageable, String symptomGroupCode, String symptomDescription) {
        Page<SdSymptomBean> pageBean;
        try {
            symptomGroupCode = StringUtils.isEmpty(symptomGroupCode) ? null : symptomGroupCode;
            symptomDescription = StringUtils.isEmpty(symptomDescription) ? null : symptomDescription;

            pageBean = sdSymptomService.searchSymptomList(pageable, symptomGroupCode, symptomDescription);
        } catch (AuthorityNotFoundException e) {
            return new PageData<>(e.getMessage());
        }

        // populate content
        List<SdSymptomBean> beanList = pageBean.getContent();
        return new PageData<>(buildSymptomDataList(beanList), pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public SdSymptomData getSymptomBySymptomCode(String symptomCode) {
        SdSymptomBean bean = sdSymptomService.getSymptomBySymptomCode(symptomCode);
        if (bean == null) {
            return null;
        }
        SdSymptomData data = new SdSymptomData();
        symptomDataPopulator.populate(bean, data);
        return data;
    }

    @Override
    public String editSymptomMapping(UpdateSymptomFormData symptomFormData) {
        String symptomCode = symptomFormData.getSymptomCode();
        String symptomGroupCode = symptomFormData.getSymptomGroupCode();
        String symptomDescription = symptomFormData.getSymptomDescription();
        String oldSymptomCode = symptomFormData.getOldSymptomCode();
        List<String> serviceTypeList = symptomFormData.getServiceTypeList();

        if (StringUtils.isEmpty(symptomCode)) {
            return "Empty Symptom Code.";
        } else if (StringUtils.isEmpty(symptomGroupCode)) {
            return "Empty Symptom Group Code.";
        } else if (StringUtils.isEmpty(symptomDescription)) {
            return "Empty Symptom Description.";
        }

        try {
            sdSymptomService.updateSymptom(oldSymptomCode, symptomCode, symptomGroupCode, symptomDescription);
        } catch (DuplicateKeyException e){
            return "Symptom Code already exists.";
        }

        try {
            sdSymptomService.editSymptomMapping(oldSymptomCode, symptomCode, serviceTypeList);
        } catch (Exception e){
            LOG.error(e.getMessage());
            return "Edit failed.";
        }

        return null;
    }

    @Override
    public EditResultData getSymptomMapping(String symptomCode) {
        List<String> results;
        try {
            List<SdSymptomMappingBean> userRoleBeanList = sdSymptomService.getSymptomMapping(symptomCode);
            if (CollectionUtils.isEmpty(userRoleBeanList)) {
                return EditResultData.error("Symptom Mapping not found.");
            }
            results = userRoleBeanList.stream().map(bean -> {
                SdSymptomMappingData data = new SdSymptomMappingData();
                symptomMappingDataPopulator.populate(bean, data);
                return data;
            }).collect(Collectors.toList())
                    .stream()
                    .map(SdSymptomMappingData::getServiceTypeCode)
                    .collect(Collectors.toList());
        } catch (InsufficientAuthorityException e) {
            return EditResultData.error(e.getMessage());
        }

        return EditResultData.dataList(results);
    }

    @Override
    public List<SdSymptomData> getAllSymptomList() {
        List<SdSymptomBean> beanList = sdSymptomService.getAllSymptomList();
        return buildSymptomDataList(beanList);
    }

    private List<SdSymptomData> buildSymptomDataList(List<SdSymptomBean> beanList) {
        if (CollectionUtils.isEmpty(beanList)) {
            return null;
        }

        // populate content
        List<SdSymptomData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdSymptomBean bean : beanList) {
                SdSymptomData data = new SdSymptomData();
                symptomDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }
        return dataList;
    }
}
