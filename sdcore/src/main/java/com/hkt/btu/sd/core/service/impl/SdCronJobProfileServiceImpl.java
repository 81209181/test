package com.hkt.btu.sd.core.service.impl;


import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.impl.BtuCronJobProfileServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdCronJobEntity;
import com.hkt.btu.sd.core.dao.mapper.SdCronJobMapper;
import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdCronJobLogService;
import com.hkt.btu.sd.core.service.SdCronJobProfileService;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;
import com.hkt.btu.sd.core.service.bean.SdSiteConfigBean;
import com.hkt.btu.sd.core.service.populator.SdCronJobProfileBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class SdCronJobProfileServiceImpl extends BtuCronJobProfileServiceImpl implements SdCronJobProfileService {
    private static final Logger LOG = LogManager.getLogger(SdCronJobProfileServiceImpl.class);


    @Resource
    SdCronJobMapper sdCronJobMapper;

    @Resource(name = "siteConfigService")
    SdSiteConfigService sdSiteConfigService;
    @Resource(name = "userService")
    SdUserService sdUserService;
    @Resource(name = "cronJobLogService")
    SdCronJobLogService sdCronJobLogService;

    @Resource(name = "cronJobProfileBeanPopulator")
    SdCronJobProfileBeanPopulator sdCronJobProfileBeanPopulator;


    @Override
    public List<BtuCronJobProfileBean> getAll() {
        List<SdCronJobEntity> entityList = sdCronJobMapper.getAll();
        if(CollectionUtils.isEmpty(entityList)){
            return new ArrayList<>();
        }
        List<BtuCronJobProfileBean> beanList = new ArrayList<>();
        for (SdCronJobEntity entity : entityList) {
            SdCronJobProfileBean bean = new SdCronJobProfileBean();
            sdCronJobProfileBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }
        return beanList;
    }

    @Override
    public BtuCronJobProfileBean getProfileBeanByGrpAndName(String jobGroup, String jobName) {
        SdCronJobEntity entity = sdCronJobMapper.getJobByJobGrpJobName(jobGroup, jobName);
        if(entity==null){
            return null;
        }

        SdCronJobProfileBean bean = new SdCronJobProfileBean();
        sdCronJobProfileBeanPopulator.populate(entity, bean);
        return bean;
    }

    @Override
    @Transactional
    public void activateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        int updateCount = updateJobProfileStatus(jobGroup, jobName, SdCronJobEntity.STATUS.ACTIVE);
        if(updateCount<1){
            throw new InvalidInputException("Cannot activate job: " + jobGroup + ", " + jobName);
        }

        // log
        sdCronJobLogService.logUserActivateJob(jobGroup, jobName);
    }

    @Override
    @Transactional
    public void deactivateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        int updateCount = updateJobProfileStatus(jobGroup, jobName, SdCronJobEntity.STATUS.DISABLE);
        if(updateCount<1){
            throw new InvalidInputException("Cannot deactivate job: " + jobGroup + ", " + jobName);
        }

        // log
        sdCronJobLogService.logUserDeactivateJob(jobGroup, jobName);
    }

    private int updateJobProfileStatus(String jobGroup, String jobName, String status){
        Integer modifyby = sdUserService.getCurrentUserUserId();
        return sdCronJobMapper.updateStatus(jobGroup, jobName, status, modifyby);
    }


}
