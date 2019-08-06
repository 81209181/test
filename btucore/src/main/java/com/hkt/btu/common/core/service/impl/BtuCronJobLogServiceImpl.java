package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuConJobProfileService;
import com.hkt.btu.common.core.service.BtuCronJobLogService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.dao.entity.BtuCronJobLogEntity;
import com.hkt.btu.common.core.dao.entity.BtuUserEntity;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.dao.mapper.BtuCronJobLogMapper;
import com.hkt.btu.common.core.dao.populator.BtuCronJobLogEntityPopulator;
import org.quartz.JobDetail;

import javax.annotation.Resource;

public class BtuCronJobLogServiceImpl implements BtuCronJobLogService {

    @Resource
    BtuCronJobLogMapper cronJobLogMapper;

    @Resource(name = "userService")
    BtuUserService userService;

    @Resource(name = "cronJobLogEntityPopulator")
    BtuCronJobLogEntityPopulator cronJobLogEntityPopulator;

    @Resource(name = "cronJobProfileService")
    BtuConJobProfileService cronJobProfileService;

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;

    @Override
    public void logUserActivateJob(String jobGroup, String jobName) {
        Integer userId = userService.getCurrentUserUserId();
        logProfileChange(jobGroup, jobName, userId, BtuCronJobLogEntity.ACTION.ACTIVATE);
    }

    @Override
    public void logUserDeactivateJob(String jobGroup, String jobName) {
        Integer userId = userService.getCurrentUserUserId();
        logProfileChange(jobGroup, jobName, userId, BtuCronJobLogEntity.ACTION.DEACTIVATE);
    }

    @Override
    public void logUserPauseJob(JobDetail jobDetail) {
        Integer userId = userService.getCurrentUserUserId();
        logInstanceChange(jobDetail, userId, BtuCronJobLogEntity.ACTION.PAUSE);
    }

    @Override
    public void logUserResumeJob(JobDetail jobDetail) {
        Integer userId = userService.getCurrentUserUserId();
        logInstanceChange(jobDetail, userId, BtuCronJobLogEntity.ACTION.RESUME);
    }

    @Override
    public void logUserTriggerJob(JobDetail jobDetail) {
        Integer userId = userService.getCurrentUserUserId();
        logInstanceChange(jobDetail, userId, BtuCronJobLogEntity.ACTION.TRIGGER);
    }

    @Override
    public void logSkip(JobDetail jobDetail) {
        logInstanceChange(jobDetail, BtuUserEntity.SYSTEM.USER_ID, BtuCronJobLogEntity.ACTION.COMPLETE);
    }

    @Override
    public void logComplete(JobDetail jobDetail) {
        logInstanceChange(jobDetail, BtuUserEntity.SYSTEM.USER_ID, BtuCronJobLogEntity.ACTION.COMPLETE);

    }

    @Override
    public void logError(JobDetail jobDetail) {
        logInstanceChange(jobDetail, BtuUserEntity.SYSTEM.USER_ID, BtuCronJobLogEntity.ACTION.ERROR);

    }

    private void logInstanceChange(JobDetail jobDetail, Integer createBy, String action) {
        // prepare insert param
        BtuCronJobLogEntity cronJobLogEntity = buildNewSdCronJobLogEntity(createBy, action);
        cronJobLogEntityPopulator.populate(jobDetail, cronJobLogEntity);

        // insert
        cronJobLogMapper.insertLog(cronJobLogEntity);
    }

    private void logProfileChange(String jobGroup, String jobName, Integer createby, String action){
        // get profile info
        BtuCronJobProfileBean profileBean = cronJobProfileService.getProfileBeanByGrpAndName(jobGroup, jobName);

        // prepare insert param
        BtuCronJobLogEntity cronJobLogEntity = buildNewSdCronJobLogEntity(createby, action);
        cronJobLogEntityPopulator.populate(profileBean, cronJobLogEntity);

        // insert
        cronJobLogMapper.insertLog(cronJobLogEntity);
    }


    private BtuCronJobLogEntity buildNewSdCronJobLogEntity(Integer createBy, String action){
        // get server info
        BtuSiteConfigBean siteConfigBean = siteConfigService.getSiteConfigBean();

        BtuCronJobLogEntity cronJobLogEntity = new BtuCronJobLogEntity();
        cronJobLogEntityPopulator.populate(siteConfigBean, cronJobLogEntity);
        cronJobLogEntity.setAction(action);
        cronJobLogEntity.setCreateby(createBy);
        return cronJobLogEntity;
    }
}
