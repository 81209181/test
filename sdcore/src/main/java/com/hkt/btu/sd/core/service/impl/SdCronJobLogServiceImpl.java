package com.hkt.btu.sd.core.service.impl;


import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.service.impl.BtuCronJobLogServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdCronJobLogEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import com.hkt.btu.sd.core.dao.mapper.SdCronJobLogMapper;
import com.hkt.btu.sd.core.dao.populator.SdCronJobLogEntityPopulator;
import com.hkt.btu.sd.core.service.SdCronJobLogService;
import com.hkt.btu.sd.core.service.SdCronJobProfileService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;

import javax.annotation.Resource;

public class SdCronJobLogServiceImpl extends BtuCronJobLogServiceImpl implements SdCronJobLogService {
    private static final Logger LOG = LogManager.getLogger(SdCronJobLogServiceImpl.class);


    @Resource
    SdCronJobLogMapper sdCronJobLogMapper;

    @Resource(name = "cronJobLogEntityPopulator")
    SdCronJobLogEntityPopulator sdCronJobLogEntityPopulator;

    @Resource(name = "userService")
    SdUserService sdUserService;
    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;
    @Resource(name = "cronJobProfileService")
    SdCronJobProfileService sdCronJobProfileService;


    @Override
    public void logUserActivateJob(String jobGroup, String jobName) {
        super.logUserActivateJob(jobGroup, jobName);
        Integer userId = sdUserService.getCurrentUserUserId();
        logProfileChange(jobGroup, jobName, userId, SdCronJobLogEntity.ACTION.ACTIVATE);
    }

    @Override
    public void logUserDeactivateJob(String jobGroup, String jobName) {
        super.logUserDeactivateJob(jobGroup, jobName);
        Integer userId = sdUserService.getCurrentUserUserId();
        logProfileChange(jobGroup, jobName, userId, SdCronJobLogEntity.ACTION.DEACTIVATE);
    }

    @Override
    public void logUserPauseJob(JobDetail jobDetail) {
        super.logUserPauseJob(jobDetail);
        Integer userId = sdUserService.getCurrentUserUserId();
        logInstanceChange(jobDetail, userId, SdCronJobLogEntity.ACTION.PAUSE);
    }

    @Override
    public void logUserResumeJob(JobDetail jobDetail) {
        super.logUserResumeJob(jobDetail);
        Integer userId = sdUserService.getCurrentUserUserId();
        logInstanceChange(jobDetail, userId, SdCronJobLogEntity.ACTION.RESUME);
    }

    @Override
    public void logUserTriggerJob(JobDetail jobDetail) {
        super.logUserTriggerJob(jobDetail);
        Integer userId = sdUserService.getCurrentUserUserId();
        logInstanceChange(jobDetail, userId, SdCronJobLogEntity.ACTION.TRIGGER);
    }

    @Override
    public void logSkip(JobDetail jobDetail) {
        super.logSkip(jobDetail);
        logInstanceChange(jobDetail, SdUserEntity.SYSTEM.USER_ID, SdCronJobLogEntity.ACTION.SKIP);
    }

    @Override
    public void logComplete(JobDetail jobDetail) {
        super.logComplete(jobDetail);
        logInstanceChange(jobDetail, SdUserEntity.SYSTEM.USER_ID, SdCronJobLogEntity.ACTION.COMPLETE);
    }

    @Override
    public void logError(JobDetail jobDetail) {
        super.logError(jobDetail);
        logInstanceChange(jobDetail, SdUserEntity.SYSTEM.USER_ID, SdCronJobLogEntity.ACTION.ERROR);
    }

    private void logInstanceChange(JobDetail jobDetail, Integer createby, String action) {
        // prepare insert param
        SdCronJobLogEntity sdCronJobLogEntity = buildNewSdCronJobLogEntity(createby, action);
        sdCronJobLogEntityPopulator.populate(jobDetail, sdCronJobLogEntity);

        // insert
        LOG.info("LOG INSTANCE CHANGE :{},{},{},{},{},{}",
                sdCronJobLogEntity.getServerIp(),
                sdCronJobLogEntity.getJobGroup(),
                sdCronJobLogEntity.getJobName(),
                sdCronJobLogEntity.getJobClass(),
                sdCronJobLogEntity.getAction(),
                sdCronJobLogEntity.getCreateby()
        );
        sdCronJobLogMapper.insertLog(sdCronJobLogEntity);
    }

    private void logProfileChange(String jobGroup, String jobName, Integer createby, String action) {
        // get profile info
        BtuCronJobProfileBean profileBean = sdCronJobProfileService.getProfileBeanByGrpAndName(jobGroup, jobName);

        // prepare insert param
        SdCronJobLogEntity sdCronJobLogEntity = buildNewSdCronJobLogEntity(createby, action);
        sdCronJobLogEntityPopulator.populate((SdCronJobProfileBean) profileBean, sdCronJobLogEntity);

        // insert
        LOG.info("LOG PROFILE CHANGE :{},{},{},{},{},{}",
                sdCronJobLogEntity.getServerIp(),
                sdCronJobLogEntity.getJobGroup(),
                sdCronJobLogEntity.getJobName(),
                sdCronJobLogEntity.getJobClass(),
                sdCronJobLogEntity.getAction(),
                sdCronJobLogEntity.getCreateby()
        );
        sdCronJobLogMapper.insertLog(sdCronJobLogEntity);
    }

    private SdCronJobLogEntity buildNewSdCronJobLogEntity(Integer createby, String action){
        // get server info
        BtuSiteConfigBean btuSiteConfigBean = siteConfigService.getSiteConfigBean();

        SdCronJobLogEntity sdCronJobLogEntity = new SdCronJobLogEntity();
        sdCronJobLogEntityPopulator.populate(btuSiteConfigBean, sdCronJobLogEntity);
        sdCronJobLogEntity.setAction(action);
        sdCronJobLogEntity.setCreateby(createby);
        return sdCronJobLogEntity;
    }

}
