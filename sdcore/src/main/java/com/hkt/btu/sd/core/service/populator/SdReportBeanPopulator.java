package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.bean.BtuSqlReportBean;
import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdCronJobEntity;
import com.hkt.btu.sd.core.dao.entity.SdSqlReportEntity;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;
import org.apache.commons.lang3.StringUtils;

public class SdReportBeanPopulator extends AbstractBeanPopulator<SdSqlReportBean> {

    public void populate(SdSqlReportEntity source, SdSqlReportBean target) {
        super.populate(source, target);

        target.setReportId(source.getReportId());
        target.setReportName(source.getReportName());
        target.setCronExp(source.getCronExp());
        target.setEmailTo(source.getEmailTo());
        target.setExportTo(source.getExportTo());
        target.setSql(source.getSql());
        target.setStatus(source.getStatus());
    }

    public void poluate(SdSqlReportBean source, SdCronJobProfileBean target) {
        target.setKeyGroup(BtuSqlReportBean.KEY_GROUP);
        target.setKeyName(source.getReportName());
        target.setJobClass("com.hkt.btu.sd.core.job.SdSqlReportJob");

        target.setStatus(source.getStatus());
        target.setActive(StringUtils.equals(source.getStatus(), SdCronJobEntity.STATUS.ACTIVE));
        target.setCronExp(source.getCronExp());
    }
}
