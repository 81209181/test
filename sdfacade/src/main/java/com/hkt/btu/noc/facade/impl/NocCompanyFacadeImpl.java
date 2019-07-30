package com.hkt.btu.noc.facade.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.noc.core.exception.CompanyNotFoundException;
import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.NocCompanyService;
import com.hkt.btu.noc.core.service.NocInputCheckService;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.facade.NocCompanyFacade;
import com.hkt.btu.noc.facade.data.CreateResultData;
import com.hkt.btu.noc.facade.data.NocCompanyData;
import com.hkt.btu.noc.facade.populator.NocCompanyDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class NocCompanyFacadeImpl implements NocCompanyFacade {
    private static final Logger LOG = LogManager.getLogger(NocCompanyFacadeImpl.class);

    @Autowired
    NocCompanyService nocCompanyService;
    @Autowired
    NocInputCheckService nocInputCheckService;

    @Autowired
    NocCompanyDataPopulator nocCompanyDataPopulator;


    @Override
    public NocCompanyData getCompanyId(Integer companyId) {
        try {
            NocCompanyBean nocCompanyBean = nocCompanyService.getCompanyById(companyId);

            NocCompanyData companyData = new NocCompanyData();
            nocCompanyDataPopulator.populate(nocCompanyBean, companyData);
            return companyData;
        } catch (CompanyNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public PageData<NocCompanyData> searchCompany(Pageable pageable, Integer companyId, String name) {
        name = StringUtils.isEmpty(name) ? null : name;

        Page<NocCompanyBean> pageBean = nocCompanyService.searchCompany(pageable, companyId, name);

        // populate content
        List<NocCompanyBean> beanList = pageBean.getContent();
        List<NocCompanyData> dataList = new LinkedList<>();
        if(!CollectionUtils.isEmpty(beanList)){
            for(NocCompanyBean bean : beanList){
                NocCompanyData data = new NocCompanyData();
                nocCompanyDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public CreateResultData createCompany(NocCompanyData nocCompanyData) {
        if(nocCompanyData==null){
            LOG.warn("Null NocCompanyData.");
            return null;
        }

        String companyName = nocCompanyData.getName();
        String remarks = nocCompanyData.getRemarks();
        try {
            nocInputCheckService.checkCompanyName(companyName);
        } catch (InvalidInputException e){
            return CreateResultData.of(e.getMessage());
        }

        // create new company
        Integer newCompanyId;
        try{
            newCompanyId = nocCompanyService.createCompany(companyName, remarks);
        } catch (DuplicateKeyException e){
            String errorMsg = "Company name " + companyName + " is already being used.";
            return CreateResultData.of(errorMsg);
        }

        return CreateResultData.of(newCompanyId);
    }

    @Override
    public String editCompany(NocCompanyData nocCompanyData) {
        if(nocCompanyData==null){
            return "Null NocCompanyData.";
        }

        Integer companyId = nocCompanyData.getCompanyId();
        String companyName = nocCompanyData.getName();
        String remarks = nocCompanyData.getRemarks();
        try {
            nocInputCheckService.checkCompanyName(companyName);
        } catch (InvalidInputException e){
            return e.getMessage();
        }

        try {
            nocCompanyService.updateCompany(companyId, companyName, remarks);
        } catch (CompanyNotFoundException | UserNotFoundException e){
            return e.getMessage();
        } catch (DuplicateKeyException e){
            return "Company name " + companyName + " is already being used.";
        }

        // no error
        return null;
    }
}
