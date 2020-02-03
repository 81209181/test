package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.dao.entity.BtuReportProfileEntity;
import com.hkt.btu.common.core.service.bean.BtuReportProfileBean;
import com.hkt.btu.common.core.service.constant.BtuJobStatusEnum;
import org.apache.commons.lang3.StringUtils;

public class BtuReportProfileBeanPopulator extends AbstractBeanPopulator<BtuReportProfileBean> {
    public void populate(BtuReportProfileEntity source, BtuReportProfileBean target) {
        super.populate(source, target);

        target.setReportId(source.getReportId());
        target.setReportName(source.getReportName());
        target.setCronExp(source.getCronExp());
        target.setEmailTo(source.getEmailTo());
        target.setSql(source.getSql());

        BtuJobStatusEnum jobStatusEnum = BtuJobStatusEnum.getEnum(source.getStatus());
        target.setStatus(jobStatusEnum);

        if(StringUtils.isEmpty(source.getReportClass())){
            target.setJobClass(BtuReportProfileBean.SQL_REPORT_DEFAULT_JOB);
        } else {
            target.setJobClass(source.getReportClass());
        }
    }
}
