package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.BtuMissingImplException;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuCronJobProfileService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

public class BtuCronJobProfileServiceImpl implements BtuCronJobProfileService {
    public static final Logger LOG = LogManager.getLogger(BtuCronJobProfileServiceImpl.class);

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;

    protected List<BtuCronJobProfileBean> getJobProfileBeanInternal(String jobGroup, String jobName){
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();

//        // create sample job
//        BtuCronJobProfileBean sampleJobBean = new BtuCronJobProfileBean();
//        sampleJobBean.setJobClass("com.hkt.btu.common.core.job.BtuSampleJob");
//        sampleJobBean.setKeyName("BtuSampleJob");
//        sampleJobBean.setKeyGroup("SYSTEM");
//        sampleJobBean.setCronExp("0 0/5 * * * ?");
//        sampleJobBean.setActive(true);
//        sampleJobBean.setMandatory(true);
//
//        // return job bean list
//        List<BtuCronJobProfileBean> beanList = new ArrayList<>();
//        beanList.add(sampleJobBean);
//        return beanList;
    }

    @Override
    public List<BtuCronJobProfileBean> getAllJobProfile() {
        LOG.info("Getting all job profiles...");
        return getJobProfileBeanInternal(null, null);
    }

    @Override
    public BtuCronJobProfileBean getProfileBeanByGrpAndName(String jobGroup, String jobName) {
        if(StringUtils.isEmpty(jobGroup)){
            LOG.error("Empty jobGroup. (group={}, name={})", jobGroup, jobName);
            return null;
        } else if(StringUtils.isEmpty(jobName)){
            LOG.error("Empty jobName. (group={}, name={})", jobGroup, jobName);
            return null;
        }

        LOG.info("Getting job profile... (group={}, name={})", jobGroup, jobName);
        List<BtuCronJobProfileBean> beanList = getJobProfileBeanInternal(jobGroup, jobName);
        if(CollectionUtils.isEmpty(beanList)){
            LOG.error("Cannot find job profile. (group={}, name={})", jobGroup, jobName);
            return null;
        }

        return beanList.get(0);
    }


    private boolean isWrongHostToRunJob(BtuCronJobProfileBean sdCronJobProfileBean) {
        BtuSiteConfigBean sdSiteConfigBean = siteConfigService.getSiteConfigBean();
        // get current server hostname
        String serverHostname = sdSiteConfigBean.getServerHostname();
        // get target cronjob server hostname
        String targetHostname = sdSiteConfigBean.getCronjobHostname();

        boolean isMandatory = sdCronJobProfileBean.isMandatory();
        boolean isTargetHost = StringUtils.equals(serverHostname, targetHostname);

        boolean isWrongHostToRunJob = !isMandatory && !isTargetHost;
        if (isWrongHostToRunJob) {
            LOG.info("Not mandatory job. " +
                    "Not cron job target host. (server: " + serverHostname + ", target: " + targetHostname + ")");
        }

        return isWrongHostToRunJob;

    }

    @Override
    public boolean isRunnable(BtuCronJobProfileBean sdCronJobProfileBean) {
        boolean isWrongHostToRunJob = isWrongHostToRunJob(sdCronJobProfileBean);
        boolean isActiveProfile = sdCronJobProfileBean.isActive();

        return isActiveProfile && !isWrongHostToRunJob;
    }

    @Override
    public boolean isRunnable(String jobGroup, String jobName) {
        BtuCronJobProfileBean sdCronJobProfileBean = getProfileBeanByGrpAndName(jobGroup, jobName);
        if (sdCronJobProfileBean == null) {
            LOG.warn("Cron job profile not found - " + jobGroup + ", " + jobName + ".");
            return false;
        }
        return isRunnable(sdCronJobProfileBean);
    }

    @Override
    public void activateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        LOG.warn("Activated job profile :{}, {}", jobGroup, jobName);
    }

    @Override
    public void deactivateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        LOG.warn("Deactivated job profile:{}, {}", jobGroup, jobName);
    }
}
