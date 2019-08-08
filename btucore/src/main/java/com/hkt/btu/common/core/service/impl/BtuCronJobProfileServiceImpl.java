package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuCronJobProfileService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class BtuCronJobProfileServiceImpl implements BtuCronJobProfileService {
    private static final Logger LOG = LogManager.getLogger(BtuCronJobProfileServiceImpl.class);

    @Resource(name = "siteConfigService")
    BtuSiteConfigService btuSiteConfigService;

    @Override
    public List<BtuCronJobProfileBean> getAll() {
        List<BtuCronJobProfileBean> beanList = new ArrayList<>();
        BtuCronJobProfileBean bean = BtuCronJobProfileBean.getSampleJob();
        beanList.add(bean);
        return beanList;
    }

    @Override
    public BtuCronJobProfileBean getProfileBeanByGrpAndName(String jobGroup, String jobName) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    @Override
    public boolean isRunnable(BtuCronJobProfileBean sdCronJobProfileBean) {
        boolean isWrongHostToRunJob = isWrongHostToRunJob(sdCronJobProfileBean);
        boolean isActiveProfile = sdCronJobProfileBean.isActive();

        return isActiveProfile && !isWrongHostToRunJob;
    }

    private boolean isWrongHostToRunJob(BtuCronJobProfileBean sdCronJobProfileBean) {
        BtuSiteConfigBean sdSiteConfigBean = btuSiteConfigService.getSiteConfigBean();
        // get current server hostname
        String serverHostname = sdSiteConfigBean.getServerHostname();
        // get target cronjob server hostname
        String targetHostname = sdSiteConfigBean.getCronjobHostname();

        boolean isMandatory = sdCronJobProfileBean.isMandatory();
        boolean isTargetHost = StringUtils.equals(serverHostname, targetHostname);

        boolean isWrongHostToRunJob = !isMandatory && !isTargetHost;
        if(isWrongHostToRunJob){
            LOG.info("Not mandatory job. " +
                    "Not cron job target host. (server: " + serverHostname + ", target: " + targetHostname + ")");
        }

        return isWrongHostToRunJob;

    }

    @Override
    public boolean isRunnable(String jobGroup, String jobName) {
        BtuCronJobProfileBean sdCronJobProfileBean = getProfileBeanByGrpAndName(jobGroup, jobName);
        if(sdCronJobProfileBean==null){
            LOG.warn("Cron job profile not found - " + jobGroup + ", " + jobName + ".");
            return false;
        }
        return isRunnable(sdCronJobProfileBean);
    }

    @Override
    public void activateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }

    @Override
    public void deactivateJobProfile(String jobGroup, String jobName) throws InvalidInputException {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
    }
}
