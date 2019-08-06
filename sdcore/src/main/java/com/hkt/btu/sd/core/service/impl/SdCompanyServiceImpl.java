package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.sd.core.dao.entity.SdCompanyEntity;
import com.hkt.btu.sd.core.dao.mapper.SdCompanyMapper;
import com.hkt.btu.sd.core.exception.CompanyNotFoundException;
import com.hkt.btu.sd.core.service.SdCompanyService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;
import com.hkt.btu.sd.core.service.populator.SdCompanyBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SdCompanyServiceImpl implements SdCompanyService {
    private static final Logger LOG = LogManager.getLogger(SdCompanyServiceImpl.class);


    @Resource
    SdCompanyMapper sdCompanyMapper;

    @Resource(name = "userService")
    SdUserService sdUserService;

    @Resource(name = "companyBeanPopulator")
    SdCompanyBeanPopulator sdCompanyBeanPopulator;

    @Override
    public SdCompanyBean getCompanyById(Integer companyId) throws CompanyNotFoundException {
        if(companyId==null){
            throw new CompanyNotFoundException("Null company id input.");
        }

        SdCompanyEntity sdCompanyEntity = sdCompanyMapper.getCompanyByCompanyId(companyId);
        if(sdCompanyEntity==null){
            throw new CompanyNotFoundException("Cannot find company with id=" + companyId + ".");
        }

        SdCompanyBean sdCompanyBean = new SdCompanyBean();
        sdCompanyBeanPopulator.populate(sdCompanyEntity, sdCompanyBean);
        return sdCompanyBean;
    }

    @Override
    public List<SdCompanyBean> getAllCompany() {
        List<SdCompanyEntity> entityList = sdCompanyMapper.getCompany(SdCompanyEntity.STATUS.ACTIVE);
        if(CollectionUtils.isEmpty(entityList)){
            return new ArrayList<>();
        }

        List<SdCompanyBean> beanList = new ArrayList<>();
        for(SdCompanyEntity sdCompanyEntity : entityList){
            SdCompanyBean sdCompanyBean = new SdCompanyBean();
            sdCompanyBeanPopulator.populate(sdCompanyEntity, sdCompanyBean);
            beanList.add(sdCompanyBean);
        }

        return beanList;
    }

    @Override
    public Page<SdCompanyBean> searchCompany(Pageable pageable, Integer companyId, String name) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        LOG.info( String.format("Searching company with {companyId: %s, name: %s}", companyId, name) );

        // get total count
        Integer totalCount = sdCompanyMapper.countSearchCompany(companyId, name);

        // get content
        List<SdCompanyEntity> sdUserEntityList = sdCompanyMapper.searchCompany(offset, pageSize, companyId, name);
        List<SdCompanyBean> sdUserBeanList = new LinkedList<>();
        if(! CollectionUtils.isEmpty(sdUserEntityList)){
            for (SdCompanyEntity sdUserEntity : sdUserEntityList){
                SdCompanyBean sdUserBean = new SdCompanyBean();
                sdCompanyBeanPopulator.populate(sdUserEntity, sdUserBean);
                sdUserBeanList.add(sdUserBean);
            }
        }

        return new PageImpl<>(sdUserBeanList, pageable, totalCount);
    }

    @Override
    @Transactional
    public void updateCompany(Integer companyId, String name, String remarks)
            throws CompanyNotFoundException, DuplicateKeyException, UserNotFoundException {
        Integer modifyby = sdUserService.getCurrentUserUserId();
        int updateCount = sdCompanyMapper.updateCompanyByCompanyId(companyId, modifyby, name, remarks);
        if(updateCount==0){
            throw new CompanyNotFoundException();
        }
    }

    @Override
    @Transactional
    public Integer createCompany(String name, String remarks)
            throws DuplicateKeyException, UserNotFoundException {
        // get current user user id
        Integer createby = sdUserService.getCurrentUserUserId();

        SdCompanyEntity sdCompanyEntity = new SdCompanyEntity();
        sdCompanyEntity.setName(name);
        sdCompanyEntity.setStatus(SdCompanyEntity.STATUS.ACTIVE);
        sdCompanyEntity.setRemarks(remarks);
        sdCompanyEntity.setCreateby(createby);

        // create company in db
        sdCompanyMapper.insertCompany(sdCompanyEntity);

        // get new company id
        return sdCompanyEntity.getCompanyId();
    }
}
