package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuSqlReportProfileService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuSqlReportBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BtuSqlReportProfileServiceImpl implements BtuSqlReportProfileService {

    public static final Logger LOG = LogManager.getLogger(BtuSqlReportProfileServiceImpl.class);

    @Override
    public List<BtuSqlReportBean> getAllReportData(String status) {
        // create sample job
        BtuSqlReportBean sampleJobBean = new BtuSqlReportBean();
        sampleJobBean.setJobClass("com.hkt.btu.common.core.job.BtuSampleJob");
        sampleJobBean.setReportName("BtuSampleReportJob");

        sampleJobBean.setCronExp("0 0/5 * * * ?");

        // return job bean list
        List<BtuSqlReportBean> beanList = new ArrayList<>();
        beanList.add(sampleJobBean);
        return beanList;
    }

    @Override
    public BtuSqlReportBean getProfileBeanByGrpAndName(String reportName) {
        LOG.warn("get profile bean by group and reportName:{}",reportName);
        return null;
    }

    @Override
    public boolean isRunnable(BtuSqlReportBean bean) {
        boolean isActiveProfile = bean.getStatus().equals(BtuSqlReportBean.ACTIVE_STATUS) ? true : false;

        return isActiveProfile;
    }

    @Override
    public boolean isRunnable(String reportId) {
        BtuSqlReportBean sqlReportBean = getProfileBeanByGrpAndName(reportId);
        if (sqlReportBean == null) {
            LOG.warn("Cron job profile not found - " + sqlReportBean.getReportName() + ".");
            return false;
        }
        return isRunnable(sqlReportBean);
    }
}
