package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuSqlReportProfileService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import java.util.ArrayList;
import java.util.List;

public class BtuSqlReportProfileServiceImpl implements BtuSqlReportProfileService {

    @Override
    public List<BtuCronJobProfileBean> getAllReportData(String status) {
        // create sample job
        BtuCronJobProfileBean sampleJobBean = new BtuCronJobProfileBean();
        sampleJobBean.setJobClass("com.hkt.btu.common.core.job.BtuSampleJob");
        sampleJobBean.setKeyName("BtuSampleReportJob");
        sampleJobBean.setKeyGroup("REPORT");
        sampleJobBean.setCronExp("0 0/5 * * * ?");
        sampleJobBean.setActive(true);
        sampleJobBean.setMandatory(true);

        // return job bean list
        List<BtuCronJobProfileBean> beanList = new ArrayList<>();
        beanList.add(sampleJobBean);
        return beanList;
    }
}
