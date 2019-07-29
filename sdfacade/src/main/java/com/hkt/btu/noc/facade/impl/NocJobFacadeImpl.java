package com.hkt.btu.noc.facade.impl;


import com.hkt.btu.noc.core.exception.InvalidInputException;
import com.hkt.btu.noc.core.service.NocCronJobProfileService;
import com.hkt.btu.noc.core.service.NocSchedulerService;
import com.hkt.btu.noc.core.service.bean.NocCronJobInstBean;
import com.hkt.btu.noc.core.service.bean.NocCronJobProfileBean;
import com.hkt.btu.noc.facade.NocJobFacade;
import com.hkt.btu.noc.facade.data.NocCronJobInstData;
import com.hkt.btu.noc.facade.data.NocCronJobProfileData;
import com.hkt.btu.noc.facade.populator.NocCronJobInstDataPopulator;
import com.hkt.btu.noc.facade.populator.NocCronJobProfileDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class NocJobFacadeImpl implements NocJobFacade {
    private static final Logger LOG = LogManager.getLogger(NocJobFacadeImpl.class);

    @Resource(name = "schedulerService")
    NocSchedulerService nocSchedulerService;
    @Resource(name = "cronJobProfileService")
    NocCronJobProfileService nocCronJobProfileService;

    @Resource(name = "cronJobInstDataPopulator")
    NocCronJobInstDataPopulator nocCronJobInstDataPopulator;
    @Resource(name = "cronJobProfileDataPopulator")
    NocCronJobProfileDataPopulator nocCronJobProfileDataPopulator;

    @Override
    public List<NocCronJobInstData> getAllJobInstance() {
        List<NocCronJobInstBean> jobBeanInstList;
        try{
            jobBeanInstList = nocSchedulerService.getAllCronJobInstance();
            if(CollectionUtils.isEmpty(jobBeanInstList)){
                return new ArrayList<>();
            }
        }catch (SchedulerException | ClassCastException e){
            LOG.error(e.getMessage(), e);
            return null;
        }

        // populate
        List<NocCronJobInstData> jobDataList = new LinkedList<>();
        for(NocCronJobInstBean cronJobInstBean : jobBeanInstList){
            NocCronJobInstData jobData = new NocCronJobInstData();
            nocCronJobInstDataPopulator.populate(cronJobInstBean, jobData);
            jobDataList.add(jobData);
        }
        return jobDataList;
    }

    @Override
    public List<NocCronJobProfileData> getAllJobProfile() {
        List<NocCronJobProfileBean> jobProfileBeanList = nocCronJobProfileService.getAll();
        if(CollectionUtils.isEmpty(jobProfileBeanList)){
            return new LinkedList<>();
        }

        // populate
        List<NocCronJobProfileData> profileDataList = new LinkedList<>();
        for(NocCronJobProfileBean bean : jobProfileBeanList){
            NocCronJobProfileData data = new NocCronJobProfileData();
            nocCronJobProfileDataPopulator.populate(bean, data);
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
            nocCronJobProfileService.activateJobProfile(keyGroup, keyName);
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
            nocCronJobProfileService.deactivateJobProfile(keyGroup, keyName);
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
            nocSchedulerService.destroyJob(keyGroup, keyName);
            nocSchedulerService.scheduleJob(keyGroup, keyName);
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
            nocSchedulerService.resumeJob(keyGroup, keyName);
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
            nocSchedulerService.pauseJob(keyGroup, keyName);
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
            nocSchedulerService.triggerJob(keyGroup, keyName);
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
