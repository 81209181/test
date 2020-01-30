package com.hkt.btu.sd.core.service.impl;


import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.impl.BtuCronJobProfileServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdCronJobEntity;
import com.hkt.btu.sd.core.dao.mapper.SdCronJobMapper;
import com.hkt.btu.sd.core.service.SdCronJobLogService;
import com.hkt.btu.sd.core.service.SdCronJobProfileService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;
import com.hkt.btu.sd.core.service.populator.SdCronJobProfileBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
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
    @Resource(name = "userService")
    SdUserService sdUserService;
    @Resource(name = "cronJobLogService")
    SdCronJobLogService sdCronJobLogService;

    @Resource(name = "cronJobProfileBeanPopulator")
    SdCronJobProfileBeanPopulator sdCronJobProfileBeanPopulator;


    @Override
    protected List<BtuCronJobProfileBean> getJobProfileBeanInternal(String jobGroup, String jobName) {
        List<SdCronJobEntity> entityList = sdCronJobMapper.getJobByJobGrpJobName(jobGroup, jobName);
        if(CollectionUtils.isEmpty(entityList)){
            LOG.warn("Found no job profile.");
            return new ArrayList<>();
        }

        List<BtuCronJobProfileBean> beanList = new ArrayList<>();
        for (SdCronJobEntity entity : entityList){
            SdCronJobProfileBean bean = new SdCronJobProfileBean();
            sdCronJobProfileBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
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
        super.activateJobProfile(jobGroup, jobName);
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
        super.deactivateJobProfile(jobGroup, jobName);
    }

    private int updateJobProfileStatus(String jobGroup, String jobName, String status){
        String modifyby = sdUserService.getCurrentUserUserId();
        return sdCronJobMapper.updateStatus(jobGroup, jobName, status, modifyby);
    }


}
