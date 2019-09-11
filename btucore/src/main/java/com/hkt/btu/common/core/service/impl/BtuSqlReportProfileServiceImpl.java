package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuSqlReportProfileService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BtuSqlReportProfileServiceImpl implements BtuSqlReportProfileService {

    public static final Logger LOG = LogManager.getLogger(BtuSqlReportProfileServiceImpl.class);

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

    @Override
    public BtuCronJobProfileBean getProfileBeanByGrpAndName(String keyGroup, String reportName) {
        LOG.warn("get profile bean by group and reportName:{},{}",keyGroup,reportName);
        return null;
    }
}