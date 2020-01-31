package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuCronJobEntity;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuReportProfileBean;
import com.hkt.btu.common.core.service.constant.BtuJobStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;

public class BtuCronJobProfileBeanPopulator extends AbstractBeanPopulator<BtuCronJobProfileBean> {
    public void populate(BtuCronJobEntity source, BtuCronJobProfileBean target) {
        super.populate(source, target);

        target.setKeyGroup(source.getJobGroup());
        target.setKeyName(source.getJobName());
        target.setJobClass(source.getJobClass());

        target.setStatus(source.getStatus());
        target.setActive( StringUtils.equals(source.getStatus(), BtuCronJobEntity.STATUS.ACTIVE) );
        target.setMandatory( StringUtils.equals(source.getMandatory(), BtuCronJobEntity.MANDATORY.YES) );
        target.setCronExp(source.getCronExp());
    }

    public void populate(BtuReportProfileBean source, BtuCronJobProfileBean target) {
        super.populate(source, target);

        target.setKeyGroup(BtuReportProfileBean.REPORT_RESERVED_JOB_GROUP);
        target.setKeyName(source.getReportName());
        target.setJobClass(source.getJobClass());

        target.setStatus(source.getStatus().getStatusCode());
        target.setActive( source.getStatus()==BtuJobStatusEnum.ACTIVE );
        target.setMandatory( false );
        target.setCronExp(source.getCronExp());
    }

    public void populate(JobDataMap jobDataMap, BtuCronJobProfileBean target) {
        target.setJobDataMap(jobDataMap);
    }
}
