package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.noc.core.dao.entity.NocCompanyEntity;
import com.hkt.btu.noc.core.dao.mapper.NocCompanyMapper;
import com.hkt.btu.noc.core.exception.CompanyNotFoundException;
import com.hkt.btu.noc.core.service.NocCompanyService;
import com.hkt.btu.noc.core.service.NocUserService;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.core.service.populator.NocCompanyBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class NocCompanyServiceImpl implements NocCompanyService {
    private static final Logger LOG = LogManager.getLogger(NocCompanyServiceImpl.class);


    @Resource
    NocCompanyMapper nocCompanyMapper;

    @Autowired
    NocUserService nocUserService;

    @Autowired
    NocCompanyBeanPopulator nocCompanyBeanPopulator;

    @Override
    public NocCompanyBean getCompanyById(Integer companyId) throws CompanyNotFoundException {
        if(companyId==null){
            throw new CompanyNotFoundException("Null company id input.");
        }

        NocCompanyEntity nocCompanyEntity = nocCompanyMapper.getCompanyByCompanyId(companyId);
        if(nocCompanyEntity==null){
            throw new CompanyNotFoundException("Cannot find company with id=" + companyId + ".");
        }

        NocCompanyBean nocCompanyBean = new NocCompanyBean();
        nocCompanyBeanPopulator.populate(nocCompanyEntity, nocCompanyBean);
        return nocCompanyBean;
    }

    @Override
    public List<NocCompanyBean> getAllCompany() {
        List<NocCompanyEntity> entityList = nocCompanyMapper.getCompany(NocCompanyEntity.STATUS.ACTIVE);
        if(CollectionUtils.isEmpty(entityList)){
            return new ArrayList<>();
        }

        List<NocCompanyBean> beanList = new ArrayList<>();
        for(NocCompanyEntity nocCompanyEntity : entityList){
            NocCompanyBean nocCompanyBean = new NocCompanyBean();
            nocCompanyBeanPopulator.populate(nocCompanyEntity, nocCompanyBean);
            beanList.add(nocCompanyBean);
        }

        return beanList;
    }

    @Override
    public Page<NocCompanyBean> searchCompany(Pageable pageable, Integer companyId, String name) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        LOG.info( String.format("Searching company with {companyId: %s, name: %s}", companyId, name) );

        // get total count
        Integer totalCount = nocCompanyMapper.countSearchCompany(companyId, name);

        // get content
        List<NocCompanyEntity> nocUserEntityList = nocCompanyMapper.searchCompany(offset, pageSize, companyId, name);
        List<NocCompanyBean> nocUserBeanList = new LinkedList<>();
        if(! CollectionUtils.isEmpty(nocUserEntityList)){
            for (NocCompanyEntity nocUserEntity : nocUserEntityList){
                NocCompanyBean nocUserBean = new NocCompanyBean();
                nocCompanyBeanPopulator.populate(nocUserEntity, nocUserBean);
                nocUserBeanList.add(nocUserBean);
            }
        }

        return new PageImpl<>(nocUserBeanList, pageable, totalCount);
    }

    @Override
    @Transactional
    public void updateCompany(Integer companyId, String name, String remarks)
            throws CompanyNotFoundException, DuplicateKeyException, UserNotFoundException {
        Integer modifyby = nocUserService.getCurrentUserUserId();
        int updateCount = nocCompanyMapper.updateCompanyByCompanyId(companyId, modifyby, name, remarks);
        if(updateCount==0){
            throw new CompanyNotFoundException();
        }
    }

    @Override
    @Transactional
    public Integer createCompany(String name, String remarks)
            throws DuplicateKeyException, UserNotFoundException {
        // get current user user id
        Integer createby = nocUserService.getCurrentUserUserId();

        NocCompanyEntity nocCompanyEntity = new NocCompanyEntity();
        nocCompanyEntity.setName(name);
        nocCompanyEntity.setStatus(NocCompanyEntity.STATUS.ACTIVE);
        nocCompanyEntity.setRemarks(remarks);
        nocCompanyEntity.setCreateby(createby);

        // create company in db
        nocCompanyMapper.insertCompany(nocCompanyEntity);

        // get new company id
        return nocCompanyEntity.getCompanyId();
    }
}
