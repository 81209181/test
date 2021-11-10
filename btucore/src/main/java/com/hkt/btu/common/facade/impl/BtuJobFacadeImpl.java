package com.hkt.btu.common.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuCronJobProfileService;
import com.hkt.btu.common.core.service.BtuSchedulerService;
import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.facade.BtuJobFacade;
import com.hkt.btu.common.facade.data.BtuCronJobInstData;
import com.hkt.btu.common.facade.data.BtuCronJobProfileData;
import com.hkt.btu.common.facade.populator.BtuCronJobInstDataPopulator;
import com.hkt.btu.common.facade.populator.BtuCronJobProfileDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BtuJobFacadeImpl implements BtuJobFacade {
    private static final Logger LOG = LogManager.getLogger(BtuJobFacadeImpl.class);

    @Resource(name = "schedulerService")
    BtuSchedulerService schedulerService;
    @Resource(name = "cronJobProfileService")
    BtuCronJobProfileService cronJobProfileService;

    @Resource(name = "cronJobInstDataPopulator")
    BtuCronJobInstDataPopulator cronJobInstDataPopulator;
    @Resource(name = "cronJobProfileDataPopulator")
    BtuCronJobProfileDataPopulator cronJobProfileDataPopulator;

    @Override
    public List<BtuCronJobInstData> getAllJobInstance() {
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
        List<BtuCronJobInstData> jobDataList = new LinkedList<>();
        for(BtuCronJobInstBean cronJobInstBean : jobBeanInstList){
            BtuCronJobInstData jobData = new BtuCronJobInstData();
            cronJobInstDataPopulator.populate(cronJobInstBean, jobData);
            jobDataList.add(jobData);
        }
        return jobDataList;
    }

    @Override
    public List<BtuCronJobProfileData> getAllJobProfile() {
        List<BtuCronJobProfileBean> jobProfileBeanList = cronJobProfileService.getAllJobProfile();
        if(CollectionUtils.isEmpty(jobProfileBeanList)){
            return new LinkedList<>();
        }

        // populate
        List<BtuCronJobProfileData> profileDataList = new LinkedList<>();
        for(BtuCronJobProfileBean bean : jobProfileBeanList){
            BtuCronJobProfileData data = new BtuCronJobProfileData();
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
            cronJobProfileService.activateJobProfile(keyGroup, keyName);
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
            cronJobProfileService.deactivateJobProfile(keyGroup, keyName);
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
        }catch (SchedulerException | InvalidInputException e){
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        try {
            BtuCronJobProfileBean jobProfileBean = cronJobProfileService.getProfileBeanByGrpAndName(keyGroup, keyName);
            schedulerService.scheduleJob(jobProfileBean);
        }catch (SchedulerException | ClassNotFoundException e){
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
