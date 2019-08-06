package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.CompanyNotFoundException;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdCompanyService;
import com.hkt.btu.sd.core.service.SdInputCheckService;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;
import com.hkt.btu.sd.facade.SdCompanyFacade;
import com.hkt.btu.sd.facade.data.CreateResultData;
import com.hkt.btu.sd.facade.data.SdCompanyData;
import com.hkt.btu.sd.facade.populator.SdCompanyDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;


public class SdCompanyFacadeImpl implements SdCompanyFacade {
    private static final Logger LOG = LogManager.getLogger(SdCompanyFacadeImpl.class);

    @Resource(name = "companyService")
    SdCompanyService sdCompanyService;
    @Resource(name = "inputCheckService")
    SdInputCheckService sdInputCheckService;

    @Resource(name = "companyDataPopulator")
    SdCompanyDataPopulator sdCompanyDataPopulator;


    @Override
    public SdCompanyData getCompanyId(Integer companyId) {
        try {
            SdCompanyBean sdCompanyBean = sdCompanyService.getCompanyById(companyId);

            SdCompanyData companyData = new SdCompanyData();
            sdCompanyDataPopulator.populate(sdCompanyBean, companyData);
            return companyData;
        } catch (CompanyNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public PageData<SdCompanyData> searchCompany(Pageable pageable, Integer companyId, String name) {
        name = StringUtils.isEmpty(name) ? null : name;

        Page<SdCompanyBean> pageBean = sdCompanyService.searchCompany(pageable, companyId, name);

        // populate content
        List<SdCompanyBean> beanList = pageBean.getContent();
        List<SdCompanyData> dataList = new LinkedList<>();
        if(!CollectionUtils.isEmpty(beanList)){
            for(SdCompanyBean bean : beanList){
                SdCompanyData data = new SdCompanyData();
                sdCompanyDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public CreateResultData createCompany(SdCompanyData sdCompanyData) {
        if(sdCompanyData==null){
            LOG.warn("Null SdCompanyData.");
            return null;
        }

        String companyName = sdCompanyData.getName();
        String remarks = sdCompanyData.getRemarks();
        try {
            sdInputCheckService.checkCompanyName(companyName);
        } catch (InvalidInputException e){
            return CreateResultData.of(e.getMessage());
        }

        // create new company
        Integer newCompanyId;
        try{
            newCompanyId = sdCompanyService.createCompany(companyName, remarks);
        } catch (DuplicateKeyException e){
            String errorMsg = "Company name " + companyName + " is already being used.";
            return CreateResultData.of(errorMsg);
        }

        return CreateResultData.of(newCompanyId);
    }

    @Override
    public String editCompany(SdCompanyData sdCompanyData) {
        if(sdCompanyData==null){
            return "Null SdCompanyData.";
        }

        Integer companyId = sdCompanyData.getCompanyId();
        String companyName = sdCompanyData.getName();
        String remarks = sdCompanyData.getRemarks();
        try {
            sdInputCheckService.checkCompanyName(companyName);
        } catch (InvalidInputException e){
            return e.getMessage();
        }

        try {
            sdCompanyService.updateCompany(companyId, companyName, remarks);
        } catch (CompanyNotFoundException | UserNotFoundException e){
            return e.getMessage();
        } catch (DuplicateKeyException e){
            return "Company name " + companyName + " is already being used.";
        }

        // no error
        return null;
    }
}
