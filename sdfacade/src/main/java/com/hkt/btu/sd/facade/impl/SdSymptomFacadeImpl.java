package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.bean.SdSortBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomGroupBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomMappingBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomWorkingPartyMappingBean;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.populator.SdSymptomDataPopulator;
import com.hkt.btu.sd.facade.populator.SdSymptomMappingDataPopulator;
import com.hkt.btu.sd.facade.populator.SdSymptomWorkingPartyMappingDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    @Resource(name = "symptomWorkingPartyMappingDataPopulator")
    SdSymptomWorkingPartyMappingDataPopulator symptomWorkingPartyMappingDataPopulator;

    @Override
    public List<SdSymptomData> getSymptomGroupList() {
        List<SdSymptomBean> beanList = sdSymptomService.getSymptomGroupList();
        return buildSymptomDataList(beanList);
    }

    @Override
    public String createSymptom(String symptomGroupCode, String symptomDescription, List<String> serviceTypeList) {
        return sdSymptomService.createSymptom(symptomGroupCode, symptomDescription, serviceTypeList);
    }


    @Override
    public PageData<SdSymptomData> searchSymptomList(Pageable pageable, String symptomGroupCode, String symptomDescription,
                                                     String strDirList, String strSortList) {
        Page<SdSymptomBean> pageBean;
        try {
            List<SdSortBean> sortList = getSortList(strDirList, strSortList);
            LOG.info("{}", sortList.toString());
            symptomGroupCode = StringUtils.isEmpty(symptomGroupCode) ? null : symptomGroupCode;
            symptomDescription = StringUtils.isEmpty(symptomDescription) ? null : symptomDescription;

            pageBean = sdSymptomService.searchSymptomList(pageable, symptomGroupCode, symptomDescription, sortList);
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
    public String editSymptomMapping(SdUpdateSymptomFormData symptomFormData) {
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
        // populate content
        List<SdSymptomData> dataList = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(beanList)) {
            for (SdSymptomBean bean : beanList) {
                SdSymptomData data = new SdSymptomData();
                symptomDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }
        return dataList;
    }

    private List<SdSortBean> getSortList(String strDirList, String strSortList) {
        if (StringUtils.isEmpty(strDirList) || StringUtils.isEmpty(strSortList)) {
            return null;
        }
        List <SdSortBean> sortDataList = new ArrayList<>();

        List<String> dirList = Arrays.asList(strDirList.split(","));
        List<String> sortList = Arrays.asList(strSortList.split(","));

        for (int i = 0; i < dirList.size(); i++) {
            SdSortBean sortData = new SdSortBean();
            sortData.setColumn(sortList.get(i));
            sortData.setDir(dirList.get(i));
            sortDataList.add(sortData);
        }

        return sortDataList;
    }

    @Override
    public boolean ifSymptomDescExist(SdSymptomData symptomData) {
        return sdSymptomService.ifSymptomDescExist(symptomData.getSymptomDescription(), symptomData.getSymptomGroupCode());
    }

    @Override
    public String createSymptomGroup(String symptomGroupCode, String symptomGroupName, List<String> roleList) {
        if (StringUtils.isEmpty(symptomGroupCode)) {
            return "Please input symptom group code.";
        }
        if (StringUtils.isEmpty(symptomGroupName)) {
            return "Please input symptom group name.";
        }

        try {
            sdSymptomService.createSymptomGroup(symptomGroupCode, symptomGroupName, roleList);
        } catch (DuplicateKeyException e){
            return "Symptom group already exists.";
        }
        return null;
    }

    @Override
    public Optional<SdSymptomGroupBean> getSymptomGroup(String symptomGroupCode) {
        return sdSymptomService.getSymptomGroup(symptomGroupCode);
    }

    @Override
    public String updateSymptomGroup(String symptomGroupCode, String symptomGroupName, List<String> roleList) {
        if (StringUtils.isEmpty(symptomGroupCode)) {
            return "Please input symptom group code.";
        }
        if (StringUtils.isEmpty(symptomGroupName)) {
            return "Please input symptom group name.";
        }
        Optional<SdSymptomGroupBean> symptomGroup = sdSymptomService.getSymptomGroup(symptomGroupCode);
        if (symptomGroup.isEmpty()) {
            return "SYMPTOM_GROUP not found.";
        } else {
            sdSymptomService.updateSymptomGroup(symptomGroupCode, symptomGroupName, roleList == null ? new ArrayList<>() : roleList);
            return null;
        }
    }

    @Override
    public String delSymptomGroup(String symptomGroupCode) {
        Optional<SdSymptomGroupBean> symptomGroup = sdSymptomService.getSymptomGroup(symptomGroupCode);
        if (symptomGroup.isEmpty()) {
            return "SYMPTOM_GROUP not found.";
        } else {
            sdSymptomService.delSymptomGroup(symptomGroupCode);
            return null;
        }
    }

    @Override
    public List<SdSymptomWorkingPartyMappingData> getSymptomWorkingPartyMappingList(){
        List<SdSymptomWorkingPartyMappingBean> beanList = sdSymptomService.getSymptomWorkingPartyMappingList();
        List<SdSymptomWorkingPartyMappingData> dataList = new ArrayList<>();
        beanList.forEach(bean -> {
            SdSymptomWorkingPartyMappingData data = new SdSymptomWorkingPartyMappingData();
            symptomWorkingPartyMappingDataPopulator.populate(bean, data);
            dataList.add(data);
        });
        return dataList;
    }

    @Override
    public String createSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode) {
        if (StringUtils.isEmpty(symptomCode)) {
            return "Please input symptom code.";
        }
        if (StringUtils.isEmpty(workingParty)) {
            return "Please input working party.";
        }
        if (StringUtils.isEmpty(serviceTypeCode)) {
            return "Please select on service type code.";
        }
        try {
            sdSymptomService.createSymptomWorkingPartyMapping(symptomCode, workingParty, serviceTypeCode);
        } catch (DuplicateKeyException dke) {
            return "SYMPTOM_CODE is primary key. The combination of WORKINGPARTY and SERVICE_TYPE_CODE should be unique.";
        } catch (DataAccessException dae) {
            return "Database operation failed.";
        }
        return null;
    }

    @Override
    public String updateSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode) {
        if (StringUtils.isEmpty(symptomCode)) {
            return "Please input symptom code.";
        }
        if (StringUtils.isEmpty(workingParty)) {
            return "Please input working party.";
        }
        if (StringUtils.isEmpty(serviceTypeCode)) {
            return "Please select on service type code.";
        }

        Optional<SdSymptomWorkingPartyMappingBean> optional = sdSymptomService.getSymptomWorkingPartyMapping(symptomCode);
        if (optional.isEmpty()) {
            return "SYMPTOM_WORKINGPARTY_MAPPING (symptom code: "+symptomCode+") not found.";
        } else {
            try {
                sdSymptomService.updateSymptomWorkingPartyMapping(symptomCode, workingParty, serviceTypeCode);
                return null;
            } catch (DuplicateKeyException e) {
                return "The combination of WORKINGPARTY and SERVICE_TYPE_CODE should be unique.";
            }
        }
    }

    @Override
    public SdSymptomWorkingPartyMappingData getSymptomWorkingPartyMapping(String symptomCode) {
        Optional<SdSymptomWorkingPartyMappingBean> optional = sdSymptomService.getSymptomWorkingPartyMapping(symptomCode);
        if (optional.isPresent()) {
            SdSymptomWorkingPartyMappingData data = new SdSymptomWorkingPartyMappingData();
            symptomWorkingPartyMappingDataPopulator.populate(optional.get(), data);
            return data;
        } else {
            return null;
        }
    }

    @Override
    public String delSymptomWorkingPartyMapping(String symptomCode) {
        Optional<SdSymptomWorkingPartyMappingBean> optional = sdSymptomService.getSymptomWorkingPartyMapping(symptomCode);
        if (optional.isEmpty()) {
            return "SYMPTOM_WORKINGPARTY_MAPPING not found.";
        } else {
            sdSymptomService.delSymptomWorkingPartyMapping(symptomCode);
            return null;
        }
    }
}
