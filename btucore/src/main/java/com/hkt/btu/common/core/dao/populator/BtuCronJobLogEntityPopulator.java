package com.hkt.btu.common.core.dao.populator;

import com.hkt.btu.common.core.dao.entity.BtuCronJobLogEntity;
import com.hkt.btu.common.core.dao.populator.EntityPopulator;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import org.quartz.JobDetail;
import org.quartz.JobKey;

public class BtuCronJobLogEntityPopulator implements EntityPopulator<BtuCronJobLogEntity> {

    public void populate(JobDetail jobDetail, BtuCronJobLogEntity entity) {
        if(jobDetail==null){
            return;
        }

        JobKey jobKey = jobDetail.getKey();
        if(jobKey!=null){
            entity.setJobGroup(jobKey.getGroup());
            entity.setJobName(jobKey.getName());
        }

        entity.setJobClass(jobDetail.getJobClass().getName());
    }

    public void populate(BtuCronJobProfileBean profileBean, BtuCronJobLogEntity entity) {
        if (profileBean==null){
            return;
        }

        entity.setJobGroup(profileBean.getKeyGroup());
        entity.setJobName(profileBean.getKeyName());
        entity.setJobClass(profileBean.getJobClass());
    }

    public void populate(BtuSiteConfigBean btuSiteConfigBean, BtuCronJobLogEntity entity) {
        if (btuSiteConfigBean==null){
            return;
        }
        entity.setServerIp(btuSiteConfigBean.getServerAddress());
    }

}
