package com.hkt.btu.noc.core.service.impl;


import com.hkt.btu.noc.core.dao.entity.NocCronJobEntity;
import com.hkt.btu.noc.core.dao.mapper.NocCronJobMapper;
import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.NocCronJobLogService;
import com.hkt.btu.noc.core.service.NocCronJobProfileService;
import com.hkt.btu.noc.core.service.NocSiteConfigService;
import com.hkt.btu.noc.core.service.NocUserService;
import com.hkt.btu.noc.core.service.bean.NocCronJobProfileBean;
import com.hkt.btu.noc.core.service.bean.NocSiteConfigBean;
import com.hkt.btu.noc.core.service.populator.NocCronJobProfileBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NocCronJobProfileServiceImpl implements NocCronJobProfileService {
    private static final Logger LOG = LogManager.getLogger(NocCronJobProfileServiceImpl.class);


    @Resource
    NocCronJobMapper nocCronJobMapper;

    @Resource(name = "NocSiteConfigService")
    NocSiteConfigService nocSiteConfigService;
    @Autowired
    NocUserService nocUserService;
    @Autowired
    NocCronJobLogService nocCronJobLogService;

//    @Resource(name = "cronJobProfileBeanPopulator")
    @Autowired
    NocCronJobProfileBeanPopulator nocCronJobProfileBeanPopulator;


    @Override
    public List<NocCronJobProfileBean> getAll() {
        List<NocCronJobEntity> entityList = nocCronJobMapper.getAll();
        if(CollectionUtils.isEmpty(entityList)){
            return new ArrayList<>();
        }

        List<NocCronJobProfileBean> beanList = new ArrayList<>();
        for (NocCronJobEntity entity : entityList) {
            NocCronJobProfileBean bean = new NocCronJobProfileBean();
            nocCronJobProfileBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public NocCronJobProfileBean getProfileBeanByGrpAndName(String jobGroup, String jobName) {
        NocCronJobEntity entity = nocCronJobMapper.getJobByJobGrpJobName(jobGroup, jobName);
        if(entity==null){
            return null;
        }

        NocCronJobProfileBean bean = new NocCronJobProfileBean();
        nocCronJobProfileBeanPopulator.populate(entity, bean);
        return bean;
    }

    private boolean isWrongHostToRunJob(NocCronJobProfileBean nocCronJobProfileBean){
        NocSiteConfigBean nocSiteConfigBean = nocSiteConfigService.getNocSiteConfigBean();
        // get current server hostname
        String serverHostname = nocSiteConfigBean.getServerHostname();
        // get target cronjob server hostname
        String targetHostname = nocSiteConfigBean.getCronjobHostname();

        boolean isMandatory = nocCronJobProfileBean.isMandatory();
        boolean isTargetHost = StringUtils.equals(serverHostname, targetHostname);

        boolean isWrongHostToRunJob = !isMandatory && !isTargetHost;
        if(isWrongHostToRunJob){
            LOG.info("Not mandatory job. " +
                    "Not cron job target host. (server: " + serverHostname + ", target: " + targetHostname + ")");
        }

        return isWrongHostToRunJob;
    }


    @Override
    public boolean isRunnable(NocCronJobProfileBean nocCronJobProfileBean) {
        boolean isWrongHostToRunJob = isWrongHostToRunJob(nocCronJobProfileBean);
        boolean isActiveProfile = nocCronJobProfileBean.isActive();

        return isActiveProfile && !isWrongHostToRunJob;
    }

    @Override
    public boolean isRunnable(String jobGroup, String jobName) {
        NocCronJobProfileBean nocCronJobProfileBean = getProfileBeanByGrpAndName(jobGroup, jobName);
        if(nocCronJobProfileBean==null){
            LOG.warn("Cron job profile not found - " + jobGroup + ", " + jobName + ".");
            return false;
        }

        return isRunnable(nocCronJobProfileBean);
    }

    @Override
    @Transactional
    public void activateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        int updateCount = updateJobProfileStatus(jobGroup, jobName, NocCronJobEntity.STATUS.ACTIVE);
        if(updateCount<1){
            throw new InvalidInputException("Cannot activate job: " + jobGroup + ", " + jobName);
        }

        // log
        nocCronJobLogService.logUserActivateJob(jobGroup, jobName);
    }

    @Override
    @Transactional
    public void deactivateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        int updateCount = updateJobProfileStatus(jobGroup, jobName, NocCronJobEntity.STATUS.DISABLE);
        if(updateCount<1){
            throw new InvalidInputException("Cannot deactivate job: " + jobGroup + ", " + jobName);
        }

        // log
        nocCronJobLogService.logUserDeactivateJob(jobGroup, jobName);
    }

    private int updateJobProfileStatus(String jobGroup, String jobName, String status){
        Integer modifyby = nocUserService.getCurrentUserUserId();
        return nocCronJobMapper.updateStatus(jobGroup, jobName, status, modifyby);
    }


}
