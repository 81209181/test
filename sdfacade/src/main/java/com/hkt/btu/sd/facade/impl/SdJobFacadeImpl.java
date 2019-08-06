package com.hkt.btu.sd.facade.impl;


import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuConJobProfileService;
import com.hkt.btu.common.core.service.BtuSchedulerService;
import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.sd.facade.SdJobFacade;
import com.hkt.btu.sd.facade.data.SdCronJobInstData;
import com.hkt.btu.sd.facade.data.SdCronJobProfileData;
import com.hkt.btu.sd.facade.populator.SdCronJobInstDataPopulator;
import com.hkt.btu.sd.facade.populator.SdCronJobProfileDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SdJobFacadeImpl implements SdJobFacade {
    private static final Logger LOG = LogManager.getLogger(SdJobFacadeImpl.class);

    @Resource(name = "schedulerService")
    BtuSchedulerService schedulerService;
    @Resource(name = "cronJobProfileService")
    BtuConJobProfileService conJobProfileService;

    @Resource(name = "cronJobInstDataPopulator")
    SdCronJobInstDataPopulator cronJobInstDataPopulator;
    @Resource(name = "cronJobProfileDataPopulator")
    SdCronJobProfileDataPopulator cronJobProfileDataPopulator;

    @Override
    public List<SdCronJobInstData> getAllJobInstance() {
        List<BtuCronJobInstBean> jobBeanInstList;
        try{
            jobBeanInstList = schedulerService.getAllCronJobInstance();
            if(CollectionUtils.isEmpty(jobBeanInstList)){
                return new ArrayList<>();
            }
        }catch (SchedulerException | ClassCastException e){
            LOG.error(e.getMessage(), e);
            return null;
        }

        // populate
        List<SdCronJobInstData> jobDataList = new LinkedList<>();
        for(BtuCronJobInstBean cronJobInstBean : jobBeanInstList){
            SdCronJobInstData jobData = new SdCronJobInstData();
            cronJobInstDataPopulator.populate(cronJobInstBean, jobData);
            jobDataList.add(jobData);
        }
        return jobDataList;
    }

    @Override
    public List<SdCronJobProfileData> getAllJobProfile() {
        List<BtuCronJobProfileBean> jobProfileBeanList = conJobProfileService.getAll();
        if(CollectionUtils.isEmpty(jobProfileBeanList)){
            return new LinkedList<>();
        }

        // populate
        List<SdCronJobProfileData> profileDataList = new LinkedList<>();
        for(BtuCronJobProfileBean bean : jobProfileBeanList){
            SdCronJobProfileData data = new SdCronJobProfileData();
            cronJobProfileDataPopulator.populate(bean, data);
            profileDataList.add(data);
        }
        return profileDataList;
    }

    @Override
    public String activateJob(String keyGroup, String keyName) {
        if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key group!";
        }else if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key name!";
        }

        try {
            conJobProfileService.activateJobProfile(keyGroup, keyName);
        }catch (InvalidInputException e){
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String deactivateJob(String keyGroup, String keyName) {
        if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key group!";
        }else if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key name!";
        }

        try {
            conJobProfileService.deactivateJobProfile(keyGroup, keyName);
        }catch (InvalidInputException e){
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String syncJob(String keyGroup, String keyName) {
        if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key group!";
        }else if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key name!";
        }

        try {
            schedulerService.destroyJob(keyGroup, keyName);
            schedulerService.scheduleJob(keyGroup, keyName);
        }catch (SchedulerException | InvalidInputException | ClassNotFoundException e){
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String resumeJob(String keyGroup, String keyName) {
        if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key group!";
        }else if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key name!";
        }

        try {
            schedulerService.resumeJob(keyGroup, keyName);
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        } catch (InvalidInputException e){
            LOG.error(e.getMessage());
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String pauseJob(String keyGroup, String keyName) {
        if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key group!";
        }else if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key name!";
        }

        try {
            schedulerService.pauseJob(keyGroup, keyName);
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        } catch (InvalidInputException e){
            LOG.error(e.getMessage());
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String triggerJob(String keyGroup, String keyName) {
        if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key group!";
        }else if( StringUtils.isEmpty(keyGroup) ){
            return "Empty job key name!";
        }

        try {
            schedulerService.triggerJob(keyGroup, keyName);
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        } catch (InvalidInputException e){
            LOG.error(e.getMessage());
            return e.getMessage();
        }

        return null;
    }
}
