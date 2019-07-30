package com.hkt.btu.noc.core.service.impl;


import com.hkt.btu.noc.core.dao.entity.NocCronJobLogEntity;
import com.hkt.btu.noc.core.dao.entity.NocUserEntity;
import com.hkt.btu.noc.core.dao.mapper.NocCronJobLogMapper;
import com.hkt.btu.noc.core.dao.populator.NocCronJobLogEntityPopulator;
import com.hkt.btu.noc.core.service.NocCronJobLogService;
import com.hkt.btu.noc.core.service.NocCronJobProfileService;
import com.hkt.btu.noc.core.service.NocSiteConfigService;
import com.hkt.btu.noc.core.service.NocUserService;
import com.hkt.btu.noc.core.service.bean.NocCronJobProfileBean;
import com.hkt.btu.noc.core.service.bean.NocSiteConfigBean;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NocCronJobLogServiceImpl implements NocCronJobLogService {

    @Resource
    NocCronJobLogMapper nocCronJobLogMapper;

//    @Resource(name = "cronJobLogEntityPopulator")
    @Autowired
    NocCronJobLogEntityPopulator nocCronJobLogEntityPopulator;

    @Autowired
    NocUserService nocUserService;
    @Resource(name = "NocSiteConfigService")
    NocSiteConfigService nocSiteConfigService;
//    @Resource(name = "cronJobProfileService")
    @Autowired
    NocCronJobProfileService nocCronJobProfileService;


    @Override
    public void logUserActivateJob(String jobGroup, String jobName) {
        Integer userId = nocUserService.getCurrentUserUserId();
        logProfileChange(jobGroup, jobName, userId, NocCronJobLogEntity.ACTION.ACTIVATE);
    }

    @Override
    public void logUserDeactivateJob(String jobGroup, String jobName) {
        Integer userId = nocUserService.getCurrentUserUserId();
        logProfileChange(jobGroup, jobName, userId, NocCronJobLogEntity.ACTION.DEACTIVATE);
    }

    @Override
    public void logUserPauseJob(JobDetail jobDetail) {
        Integer userId = nocUserService.getCurrentUserUserId();
        logInstanceChange(jobDetail, userId, NocCronJobLogEntity.ACTION.PAUSE);
    }

    @Override
    public void logUserResumeJob(JobDetail jobDetail) {
        Integer userId = nocUserService.getCurrentUserUserId();
        logInstanceChange(jobDetail, userId, NocCronJobLogEntity.ACTION.RESUME);
    }

    @Override
    public void logUserTriggerJob(JobDetail jobDetail) {
        Integer userId = nocUserService.getCurrentUserUserId();
        logInstanceChange(jobDetail, userId, NocCronJobLogEntity.ACTION.TRIGGER);
    }

    @Override
    public void logSkip(JobDetail jobDetail) {
        logInstanceChange(jobDetail, NocUserEntity.SYSTEM.USER_ID, NocCronJobLogEntity.ACTION.SKIP);
    }

    @Override
    public void logComplete(JobDetail jobDetail) {
        logInstanceChange(jobDetail, NocUserEntity.SYSTEM.USER_ID, NocCronJobLogEntity.ACTION.COMPLETE);
    }

    @Override
    public void logError(JobDetail jobDetail) {
        logInstanceChange(jobDetail, NocUserEntity.SYSTEM.USER_ID, NocCronJobLogEntity.ACTION.ERROR);
    }

    private void logInstanceChange(JobDetail jobDetail, Integer createby, String action){
        // prepare insert param
        NocCronJobLogEntity nocCronJobLogEntity = buildNewNocCronJobLogEntity(createby, action);
        nocCronJobLogEntityPopulator.populate(jobDetail, nocCronJobLogEntity);

        // insert
        nocCronJobLogMapper.insertLog(nocCronJobLogEntity);
    }

    private void logProfileChange(String jobGroup, String jobName, Integer createby, String action){
        // get profile info
        NocCronJobProfileBean profileBean = nocCronJobProfileService.getProfileBeanByGrpAndName(jobGroup, jobName);

        // prepare insert param
        NocCronJobLogEntity nocCronJobLogEntity = buildNewNocCronJobLogEntity(createby, action);
        nocCronJobLogEntityPopulator.populate(profileBean, nocCronJobLogEntity);

        // insert
        nocCronJobLogMapper.insertLog(nocCronJobLogEntity);
    }

    private NocCronJobLogEntity buildNewNocCronJobLogEntity(Integer createby, String action){
        // get server info
        NocSiteConfigBean nocSiteConfigBean = nocSiteConfigService.getNocSiteConfigBean();

        NocCronJobLogEntity nocCronJobLogEntity = new NocCronJobLogEntity();
        nocCronJobLogEntityPopulator.populate(nocSiteConfigBean, nocCronJobLogEntity);
        nocCronJobLogEntity.setAction(action);
        nocCronJobLogEntity.setCreateby(createby);
        return nocCronJobLogEntity;
    }

}
