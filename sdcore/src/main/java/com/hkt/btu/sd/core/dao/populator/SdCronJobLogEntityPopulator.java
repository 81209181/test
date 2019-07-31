package com.hkt.btu.sd.core.dao.populator;

import com.hkt.btu.common.core.dao.populator.EntityPopulator;
import com.hkt.btu.sd.core.dao.entity.SdCronJobLogEntity;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;
import com.hkt.btu.sd.core.service.bean.SdSiteConfigBean;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

public class SdCronJobLogEntityPopulator implements EntityPopulator<SdCronJobLogEntity> {

    public void populate(JobDetail jobDetail, SdCronJobLogEntity entity) {
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

    public void populate(SdCronJobProfileBean profileBean, SdCronJobLogEntity entity) {
        if (profileBean==null){
            return;
        }

        entity.setJobGroup(profileBean.getKeyGroup());
        entity.setJobName(profileBean.getKeyName());
        entity.setJobClass(profileBean.getJobClass());
    }

    public void populate(SdSiteConfigBean sdSiteConfigBean, SdCronJobLogEntity entity) {
        if (sdSiteConfigBean==null){
            return;
        }
        entity.setServerIp(sdSiteConfigBean.getServerAddress());
    }
}
