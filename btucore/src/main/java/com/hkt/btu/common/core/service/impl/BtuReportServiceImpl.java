package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.exception.BtuMissingImplException;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuCsvService;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BtuReportServiceImpl implements BtuReportService {
    public static final Logger LOG = LogManager.getLogger(BtuReportServiceImpl.class);

    private static final String CSV_SUFFIX = ".csv";
    private static final DateTimeFormatter FILE_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String SQL_INJ_STR = "and|exec|insert|delete|update|\n\n|chr|mid|master|truncate|char|declare|;|-|,";

    @Resource(name = "schedulerService")
    BtuSchedulerService schedulerService;
    @Resource(name = "csvService")
    BtuCsvService csvService;

    @Resource(name = "cronJobProfileBeanPopulator")
    BtuCronJobProfileBeanPopulator cronJobProfileBeanPopulator;

    protected TransactionTemplate transactionTemplate;

    public BtuReportServiceImpl(){}
    public BtuReportServiceImpl(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    protected List<BtuReportProfileBean> getReportProfileInternal(String reportId, String reportName){
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();

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
    public BtuReportProfileBean getReportProfileByReportId(String reportId) {
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
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public boolean isRunnable(BtuReportProfileBean bean) {
        return BtuJobStatusEnum.ACTIVE == bean.getStatus();
    }

    @Override
    public boolean isRunnable(String reportId) {
        BtuReportProfileBean bean = getReportProfileByReportId(reportId);
        if (bean == null) {
            LOG.warn("Report profile not found. (reportId={})", reportId);
            return false;
        }
        return isRunnable(bean);
    }

    @Override
    public String createReportProfile(String reportName, String cronExpression, BtuJobStatusEnum status,
                                      String sql, String emailTo, String remarks) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public String deleteReportProfile(String reportId) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public String updateReportProfile(String reportId, String reportName, String cronExpression, BtuJobStatusEnum status,
                                      String sql, String emailTo, String remarks) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
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
    public File getCsvFile(BtuReportMetaDataBean metaDataBean) {
        String filePath = getFilePath(metaDataBean.getExportTo(), metaDataBean.getReportName());

        String sql = metaDataBean.getSql();
        if (StringUtils.isNotBlank(sql)) {
            sql = sql.toLowerCase();
            sql = sql.replaceAll(SQL_INJ_STR, StringUtils.EMPTY);
        }

        List<Map<String, Object>> results = executeSql(sql);
        return csvService.generateCSV(filePath, null, results);
    }

    @Override
    public String getReportDirPath(BtuReportProfileBean reportProfileBean) {
        if (reportProfileBean==null) {
            LOG.error("Null reportProfileBean.");
            return null;
        }

        String reportName = reportProfileBean.getReportName();
        if (StringUtils.isEmpty(reportName)) {
            LOG.error("Empty reportName.");
            return null;
        }

        return BtuReportProfileBean.REPORT_FOLDER_PATH + reportName;
    }

    @Override
    public Stream<Path> getReportFileList(String reportId) throws IOException {
        if (StringUtils.isEmpty(reportId)) {
            LOG.error("Empty reportId.");
            return null;
        }

        BtuReportProfileBean reportProfileBean = getReportProfileByReportId(reportId);
        if (reportProfileBean == null) {
            return null;
        }

        final String REPORT_ROOT_DIR = getReportDirPath(reportProfileBean);
        if (StringUtils.isEmpty(REPORT_ROOT_DIR)) {
            return null;
        }

        LOG.info("Getting report file path stream... (reportId={}, reportDir={})", reportId, REPORT_ROOT_DIR);
        Path reportDirPath = Paths.get(REPORT_ROOT_DIR);
        return Files.walk(reportDirPath);
    }

    private String getFilePath(String dirPath, String reportName) {
        final LocalDateTime NOW = LocalDateTime.now();
        // full path: /opt/report/${reportName}/${reportName}_${yyyymmdd_hh24miss}

        // prepare file dir
        String reportDirPath = dirPath + reportName;
        File reportDir = new File(reportDirPath);
        if (!reportDir.exists()) {
            boolean succeed = reportDir.mkdirs();
            if(succeed){
                LOG.info("Created report dir: {}", reportDirPath);
            }else {
                LOG.error("Failed to create report dir: {}", reportDirPath);
            }
        }

        // prepare file path
        String formatLocalDateTime = NOW.format(FILE_TIMESTAMP_FORMATTER);
        String fileName = reportName + "_" +formatLocalDateTime + CSV_SUFFIX;

        return reportDirPath + File.separator + fileName;
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
