package com.hkt.btu.sd.core.service.impl;


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

public class SdCronJobProfileServiceImpl implements SdCronJobProfileService {
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
    public List<SdCronJobProfileBean> getAll() {
        List<SdCronJobEntity> entityList = sdCronJobMapper.getAll();
        if(CollectionUtils.isEmpty(entityList)){
            return new ArrayList<>();
        }

        List<SdCronJobProfileBean> beanList = new ArrayList<>();
        for (SdCronJobEntity entity : entityList) {
            SdCronJobProfileBean bean = new SdCronJobProfileBean();
            sdCronJobProfileBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public SdCronJobProfileBean getProfileBeanByGrpAndName(String jobGroup, String jobName) {
        SdCronJobEntity entity = sdCronJobMapper.getJobByJobGrpJobName(jobGroup, jobName);
        if(entity==null){
            return null;
        }

        SdCronJobProfileBean bean = new SdCronJobProfileBean();
        sdCronJobProfileBeanPopulator.populate(entity, bean);
        return bean;
    }

    private boolean isWrongHostToRunJob(SdCronJobProfileBean sdCronJobProfileBean){
        SdSiteConfigBean sdSiteConfigBean = sdSiteConfigService.getSdSiteConfigBean();
        // get current server hostname
        String serverHostname = sdSiteConfigBean.getServerHostname();
        // get target cronjob server hostname
        String targetHostname = sdSiteConfigBean.getCronjobHostname();

        boolean isMandatory = sdCronJobProfileBean.isMandatory();
        boolean isTargetHost = StringUtils.equals(serverHostname, targetHostname);

        boolean isWrongHostToRunJob = !isMandatory && !isTargetHost;
        if(isWrongHostToRunJob){
            LOG.info("Not mandatory job. " +
                    "Not cron job target host. (server: " + serverHostname + ", target: " + targetHostname + ")");
        }

        return isWrongHostToRunJob;
    }


    @Override
    public boolean isRunnable(SdCronJobProfileBean sdCronJobProfileBean) {
        boolean isWrongHostToRunJob = isWrongHostToRunJob(sdCronJobProfileBean);
        boolean isActiveProfile = sdCronJobProfileBean.isActive();

        return isActiveProfile && !isWrongHostToRunJob;
    }

    @Override
    public boolean isRunnable(String jobGroup, String jobName) {
        SdCronJobProfileBean sdCronJobProfileBean = getProfileBeanByGrpAndName(jobGroup, jobName);
        if(sdCronJobProfileBean==null){
            LOG.warn("Cron job profile not found - " + jobGroup + ", " + jobName + ".");
            return false;
        }

        return isRunnable(sdCronJobProfileBean);
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
