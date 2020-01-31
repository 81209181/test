package com.hkt.btu.common.facade.populator;

import com.hkt.btu.common.core.service.bean.BtuReportProfileBean;
import com.hkt.btu.common.core.service.constant.BtuJobStatusEnum;
import com.hkt.btu.common.facade.data.BtuReportProfileData;

public class BtuReportProfileDataPopulator extends AbstractDataPopulator<BtuReportProfileData> {

    public void populate(BtuReportProfileBean source, BtuReportProfileData target) {
        target.setCronExp(source.getCronExp());
        target.setStatus(source.getStatus().getDesc());
        target.setSql(source.getSql());
        target.setRemarks(source.getRemarks());
        target.setReportName(source.getReportName());
        target.setReportId(source.getReportId());
        target.setEmailTo(source.getEmailTo());
        target.setActive(source.getStatus()== BtuJobStatusEnum.ACTIVE);

        target.setRemarks(source.getRemarks());
    }
}
