package com.hkt.btu.sd.facade.impl;


import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdCronJobProfileService;
import com.hkt.btu.sd.core.service.SdSchedulerService;
import com.hkt.btu.sd.core.service.bean.SdCronJobInstBean;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;
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
    SdSchedulerService sdSchedulerService;
    @Resource(name = "cronJobProfileService")
    SdCronJobProfileService sdCronJobProfileService;

    @Resource(name = "cronJobInstDataPopulator")
    SdCronJobInstDataPopulator sdCronJobInstDataPopulator;
    @Resource(name = "cronJobProfileDataPopulator")
    SdCronJobProfileDataPopulator sdCronJobProfileDataPopulator;

    @Override
    public List<SdCronJobInstData> getAllJobInstance() {
        List<SdCronJobInstBean> jobBeanInstList;
        try{
            jobBeanInstList = sdSchedulerService.getAllCronJobInstance();
            if(CollectionUtils.isEmpty(jobBeanInstList)){
                return new ArrayList<>();
            }
        }catch (SchedulerException | ClassCastException e){
            LOG.error(e.getMessage(), e);
            return null;
        }

        // populate
        List<SdCronJobInstData> jobDataList = new LinkedList<>();
        for(SdCronJobInstBean cronJobInstBean : jobBeanInstList){
            SdCronJobInstData jobData = new SdCronJobInstData();
            sdCronJobInstDataPopulator.populate(cronJobInstBean, jobData);
            jobDataList.add(jobData);
        }
        return jobDataList;
    }

    @Override
    public List<SdCronJobProfileData> getAllJobProfile() {
        List<SdCronJobProfileBean> jobProfileBeanList = sdCronJobProfileService.getAll();
        if(CollectionUtils.isEmpty(jobProfileBeanList)){
            return new LinkedList<>();
        }

        // populate
        List<SdCronJobProfileData> profileDataList = new LinkedList<>();
        for(SdCronJobProfileBean bean : jobProfileBeanList){
            SdCronJobProfileData data = new SdCronJobProfileData();
            sdCronJobProfileDataPopulator.populate(bean, data);
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
            sdCronJobProfileService.activateJobProfile(keyGroup, keyName);
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
            sdCronJobProfileService.deactivateJobProfile(keyGroup, keyName);
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
            sdSchedulerService.destroyJob(keyGroup, keyName);
            sdSchedulerService.scheduleJob(keyGroup, keyName);
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
            sdSchedulerService.resumeJob(keyGroup, keyName);
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
            sdSchedulerService.pauseJob(keyGroup, keyName);
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
            sdSchedulerService.triggerJob(keyGroup, keyName);
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
