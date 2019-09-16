package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;
import com.hkt.btu.sd.facade.data.SdSqlReportData;

public class SdSqlReportDataPopulator extends AbstractDataPopulator<SdSqlReportData> {

    public void populate(SdSqlReportBean source, SdSqlReportData target) {
        super.populate(source, target);
        target.setCronExp(source.getCronExp());
        target.setStatus(source.getStatus());
        target.setSql(source.getSql());
        target.setRemarks(source.getRemarks());
        target.setReportName(source.getReportName());
        target.setReportId(source.getReportId());
        target.setExportTo(source.getExportTo());
        target.setEmailTo(source.getEmailTo());
        target.setActive(source.getStatus().equals(SdSqlReportBean.ACTIVE_STATUS) ? true : false);
    }
}
