package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuCronJobEntity;
import com.hkt.btu.common.core.dao.mapper.BtuCronJobMapper;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuConJobProfileService;
import com.hkt.btu.common.core.service.BtuCronJobLogService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.service.populator.BtuCronJobProfileBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class BtuConJobProfileServiceImpl implements BtuConJobProfileService {

    private static final Logger LOG = LogManager.getLogger(BtuConJobProfileServiceImpl.class);

    @Resource
    BtuCronJobMapper cronJobMapper;

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;

    @Resource(name = "userService")
    BtuUserService userService;

    @Resource(name = "cronJobLogService")
    BtuCronJobLogService cronJobLogService;

    @Resource(name = "cronJobProfileBeanPopulator")
    BtuCronJobProfileBeanPopulator cronJobProfileBeanPopulator;


    @Override
    public BtuCronJobProfileBean getProfileBeanByGrpAndName(String jobGroup, String jobName) {
        BtuCronJobEntity entity = cronJobMapper.getJobByJobGrpJobName(jobGroup, jobName);
        if(entity==null){
            return null;
        }
        BtuCronJobProfileBean bean = new BtuCronJobProfileBean();
        cronJobProfileBeanPopulator.populate(entity, bean);
        return bean;
    }

    @Override
    public List<BtuCronJobProfileBean> getAll() {
        List<BtuCronJobEntity> entityList = cronJobMapper.getAll();
        if(CollectionUtils.isEmpty(entityList)){
            return new ArrayList<>();
        }

        List<BtuCronJobProfileBean> beanList = new ArrayList<>();
        for (BtuCronJobEntity entity : entityList) {
            BtuCronJobProfileBean bean = new BtuCronJobProfileBean();
            cronJobProfileBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public boolean isRunnable(BtuCronJobProfileBean cronJobProfileBean) {
        boolean isWrongHostToRunJob = isWrongHostToRunJob(cronJobProfileBean);
        boolean isActiveProfile = cronJobProfileBean.isActive();
        return isActiveProfile && !isWrongHostToRunJob;
    }

    @Override
    public boolean isRunnable(String jobGroup, String jobName) {
        BtuCronJobProfileBean cronJobProfileBean = getProfileBeanByGrpAndName(jobGroup, jobName);
        if(cronJobProfileBean==null){
            LOG.warn("Cron job profile not found - " + jobGroup + ", " + jobName + ".");
            return false;
        }
        return isRunnable(cronJobProfileBean);
    }

    @Override
    public void activateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        int updateCount = updateJobProfileStatus(jobGroup, jobName, BtuCronJobEntity.STATUS.ACTIVE);
        if(updateCount<1){
            throw new InvalidInputException("Cannot activate job: " + jobGroup + ", " + jobName);
        }
        // log
        cronJobLogService.logUserActivateJob(jobGroup, jobName);
    }

    @Override
    public void deactivateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        int updateCount = updateJobProfileStatus(jobGroup, jobName, BtuCronJobEntity.STATUS.DISABLE);
        if(updateCount<1){
            throw new InvalidInputException("Cannot deactivate job: " + jobGroup + ", " + jobName);
        }
        // log
        cronJobLogService.logUserDeactivateJob(jobGroup, jobName);
    }

    private int updateJobProfileStatus(String jobGroup, String jobName, String status){
        Integer modifyBy = userService.getCurrentUserUserId();
        return cronJobMapper.updateStatus(jobGroup, jobName, status, modifyBy);
    }

    private boolean isWrongHostToRunJob(BtuCronJobProfileBean cronJobProfileBean){
        BtuSiteConfigBean siteConfigBean = siteConfigService.getSiteConfigBean();
        // get current server hostname
        String serverHostname = siteConfigBean.getServerHostname();
        // get target cronjob server hostname
        String targetHostname = siteConfigBean.getCronjobHostname();

        boolean isMandatory = cronJobProfileBean.isMandatory();
        boolean isTargetHost = StringUtils.equals(serverHostname, targetHostname);

        boolean isWrongHostToRunJob = !isMandatory && !isTargetHost;
        if(isWrongHostToRunJob){
            LOG.info("Not mandatory job. " +
                    "Not cron job target host. (server: " + serverHostname + ", target: " + targetHostname + ")");
        }

        return isWrongHostToRunJob;
    }
}
