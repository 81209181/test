package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuReportService;
import com.hkt.btu.common.core.service.BtuSchedulerService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuReportMetaDataBean;
import com.hkt.btu.common.core.service.bean.BtuReportProfileBean;
import com.hkt.btu.common.core.service.constant.BtuJobStatusEnum;
import com.hkt.btu.common.core.service.populator.BtuCronJobProfileBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public class BtuReportServiceImpl implements BtuReportService {
    public static final Logger LOG = LogManager.getLogger(BtuReportServiceImpl.class);

    @Resource(name = "schedulerService")
    BtuSchedulerService schedulerService;

    @Resource(name = "cronJobProfileBeanPopulator")
    BtuCronJobProfileBeanPopulator cronJobProfileBeanPopulator;

    protected TransactionTemplate transactionTemplate;

    public BtuReportServiceImpl(){}
    public BtuReportServiceImpl(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    protected List<BtuReportProfileBean> getReportProfileInternal(String reportId, String reportName){
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;

//        // create sample job
//        BtuSqlReportBean sampleJobBean = new BtuSqlReportBean();
//        sampleJobBean.setJobClass("com.hkt.btu.common.core.job.BtuSampleJob");
//        sampleJobBean.setReportName("BtuSampleReportJob");
//
//        sampleJobBean.setCronExp("0 0/5 * * * ?");
//
//        // return job bean list
//        List<BtuSqlReportBean> beanList = new ArrayList<>();
//        beanList.add(sampleJobBean);
//        return beanList;
    }

    @Override
    public List<BtuReportProfileBean> getAllReportProfiles() {
        LOG.info("Getting all report profiles...");
        return getReportProfileInternal(null, null);
    }

    @Override
    public BtuReportProfileBean getSqlReportProfileByReportId(String reportId) {
        if(StringUtils.isEmpty(reportId)){
            LOG.error("Empty reportId.");
        }

        LOG.info("Retrieving report profile... (reportId={})", reportId);
        List<BtuReportProfileBean> beanList = getReportProfileInternal(reportId, null);
        if(CollectionUtils.isEmpty(beanList)){
            LOG.error("Cannot find report profile.  (reportId={})", reportId);
            return null;
        }

        BtuReportProfileBean bean = beanList.get(0);
        LOG.info("Retrieved report profile... (reportId={}, reportName={})", reportId, bean.getReportName());
        return bean;
    }

    @Override
    public BtuReportProfileBean getSqlReportProfileByReportName(String reportName) {
        if(StringUtils.isEmpty(reportName)){
            LOG.error("Empty reportName.");
        }

        LOG.info("Retrieving report profile... (reportName={})", reportName);
        List<BtuReportProfileBean> beanList = getReportProfileInternal(null, reportName);
        if(CollectionUtils.isEmpty(beanList)){
            LOG.error("Cannot find report profile.  (reportName={})", reportName);
            return null;
        }

        BtuReportProfileBean bean = beanList.get(0);
        LOG.info("Retrieved report profile... (reportId={}, reportName={})", bean.getReportId(), reportName);
        return bean;
    }

    @Override
    public List<Map<String, Object>> executeSql(String sql) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    @Override
    public boolean isRunnable(BtuReportProfileBean bean) {
        return BtuJobStatusEnum.ACTIVE == bean.getStatus();
    }

    @Override
    public boolean isRunnable(String reportId) {
        BtuReportProfileBean bean = getSqlReportProfileByReportId(reportId);
        if (bean == null) {
            LOG.warn("Report profile not found. (reportId={})", reportId);
            return false;
        }
        return isRunnable(bean);
    }

    @Override
    public String createReportProfile(String reportName, String cronExpression, BtuJobStatusEnum status,
                                      String sql, String emailTo, String remarks) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    @Override
    public String deleteReportProfile(String reportId) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return null;
    }

    @Override
    public String updateReportProfile(String reportId, String reportName, String cronExpression, BtuJobStatusEnum status,
                                      String sql, String emailTo, String remarks) {
        LOG.warn("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        return "Cannot update report profile with btu impl.";
    }

    @Override
    @Transactional
    public String activateReportProfile(String reportId) {
        String errorMsg = updateReportProfile(reportId, null, null,
                BtuJobStatusEnum.ACTIVE, null, null, null);
        if (StringUtils.isNotEmpty(errorMsg)) {
            LOG.error(errorMsg);
            return errorMsg;
        }

        LOG.info("Activated job profile:{},{}", BtuReportProfileBean.REPORT_RESERVED_JOB_GROUP, reportId);
        return null;
    }

    @Override
    @Transactional
    public String deactivateReportProfile(String reportId) {
        String errorMsg = updateReportProfile(reportId, null, null,
                BtuJobStatusEnum.DISABLE, null, null, null);
        if(StringUtils.isNotEmpty(errorMsg)) {
            LOG.error(errorMsg);
            return errorMsg;
        }

        LOG.info("Deactivated job profile:{},{}", BtuReportProfileBean.REPORT_RESERVED_JOB_GROUP, reportId);
        return null;
    }

    @Override
    public void scheduleReportJob(BtuReportProfileBean reportProfileBean) throws SchedulerException, InvalidInputException, ClassNotFoundException {
        // prepare report job meta
        BtuReportMetaDataBean metaData = new BtuReportMetaDataBean();
        metaData.setSql(reportProfileBean.getSql());
        metaData.setExportTo(BtuReportProfileBean.REPORT_FOLDER_PATH);
        metaData.setEmailTo(reportProfileBean.getEmailTo());
        metaData.setReportName(reportProfileBean.getReportName());

        // prepare report job data map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(BtuReportProfileBean.REPORT_JOBDATAMAP_KEY, metaData);

        BtuCronJobProfileBean jobProfileBean = new BtuCronJobProfileBean();
        cronJobProfileBeanPopulator.populate(reportProfileBean, jobProfileBean);
        cronJobProfileBeanPopulator.populate(jobDataMap, jobProfileBean);
        schedulerService.scheduleJob(jobProfileBean);
    }

}
