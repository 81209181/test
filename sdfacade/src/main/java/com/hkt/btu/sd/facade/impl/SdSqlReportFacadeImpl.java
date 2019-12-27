package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdAuditTrailService;
import com.hkt.btu.sd.core.service.SdSchedulerService;
import com.hkt.btu.sd.core.service.SdSqlReportProfileService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdCronJobInstBean;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;
import com.hkt.btu.sd.facade.SdSqlReportFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.populator.SdCronJobInstDataPopulator;
import com.hkt.btu.sd.facade.populator.SdSqlReportDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.core.io.UrlResource;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SdSqlReportFacadeImpl implements SdSqlReportFacade {

    private static final Logger LOG = LogManager.getLogger(SdSqlReportFacadeImpl.class);

    @Resource(name = "reportDataPopulator")
    SdSqlReportDataPopulator reportDataPopulator;

    @Resource(name = "sqlReportProfileService")
    SdSqlReportProfileService reportService;

    @Resource(name = "schedulerService")
    SdSchedulerService sdSchedulerService;

    @Resource(name = "cronJobInstDataPopulator")
    SdCronJobInstDataPopulator sdCronJobInstDataPopulator;

    @Resource(name = "auditTrailService")
    SdAuditTrailService auditTrailService;

    @Resource(name = "userService")
    SdUserService userService;

    @Override
    public List<SdSqlReportData> getAllReportData() {
        return reportService.getAllReportBean(null)
                .stream()
                .map(report -> {
                    SdSqlReportData data = new SdSqlReportData();
                    reportDataPopulator.populate(report, data);
                    return data;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SdCronJobInstData> getAllReportJobInstance() {
        List<SdCronJobInstBean> jobBeanInstList;
        try {
            jobBeanInstList = sdSchedulerService.getAllReportJobInstance();
            if (CollectionUtils.isEmpty(jobBeanInstList)) {
                return new ArrayList<>();
            }
        } catch (SchedulerException | ClassCastException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }

        // populate
        List<SdCronJobInstData> jobDataList = new LinkedList<>();
        for (SdCronJobInstBean cronJobInstBean : jobBeanInstList) {
            SdCronJobInstData jobData = new SdCronJobInstData();
            sdCronJobInstDataPopulator.populate(cronJobInstBean, jobData);
            jobDataList.add(jobData);
        }
        return jobDataList;
    }

    @Override
    public SdSqlReportData getSqlReportDataByReportId(String reportId) {
        SdSqlReportBean bean = reportService.getSqlReportDataByReportId(reportId);
        SdSqlReportData data = new SdSqlReportData();
        reportDataPopulator.populate(bean, data);
        return data;
    }

    @Override
    public String resumeReport(String keyName) {
        if (StringUtils.isEmpty(keyName)) {
            return "Empty report name!";
        }

        try {
            sdSchedulerService.resumeJob(SdSqlReportBean.KEY_GROUP, keyName);
        } catch (SchedulerException | InvalidInputException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String pauseReport(String keyName) {
        if (StringUtils.isEmpty(keyName)) {
            return "Empty report name!";
        }

        try {
            sdSchedulerService.pauseJob(SdSqlReportBean.KEY_GROUP, keyName);
        } catch (SchedulerException | InvalidInputException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String triggerReport(String keyName) {
        if (StringUtils.isEmpty(keyName)) {
            return "Empty report name!";
        }

        try {
            sdSchedulerService.triggerJob(SdSqlReportBean.KEY_GROUP, keyName);
        } catch (SchedulerException | InvalidInputException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public ResponseReportData createReport(RequestReportData data) {
        String reportId = "";
        try {
            checkReportData(data);
            reportId = reportService.createReport(data.getReportName(), data.getCronExp(), data.getStatus(),
                    data.getSql(), data.getExportTo(), data.getEmailTo(), data.getRemarks());
            sdSchedulerService.scheduleReportJob(data.getReportName());
        } catch (InvalidInputException | SchedulerException | ClassNotFoundException e) {
            LOG.warn(e.getMessage());
            return ResponseReportData.of(null, e.getMessage());
        }
        return ResponseReportData.of(reportId, null);
    }

    @Override
    public ResponseReportData deleteReport(String reportId) {
        try {
            if (StringUtils.isEmpty(reportId)) {
                throw new InvalidInputException("Empty reportId.");
            }
            String reportName = reportService.deleteReport(reportId);
            sdSchedulerService.destroyJob(SdSqlReportBean.KEY_GROUP, reportName);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return ResponseReportData.of(null, e.getMessage());
        }
        return ResponseReportData.of(null, null);
    }

    @Override
    public ResponseReportData updateReport(RequestReportData data) {
        try {
            checkReportData(data);
            String reportName = reportService.updateReport(data.getReportId(), data.getReportName(), data.getCronExp(), data.getStatus(),
                    data.getSql(), data.getEmailTo(), data.getRemarks());
            sdSchedulerService.pauseJob(SdSqlReportBean.KEY_GROUP, reportName);
            sdSchedulerService.destroyJob(SdSqlReportBean.KEY_GROUP, reportName);
            sdSchedulerService.scheduleReportJob(data.getReportName());
        } catch (InvalidInputException | SchedulerException | ClassNotFoundException e) {
            LOG.warn(e.getMessage());
            return ResponseReportData.of(null, e.getMessage());
        }
        return ResponseReportData.of(data.getReportId(), null);
    }

    @Override
    public String activeReport(String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            return "Empty reportId!";
        }

        try {
            reportService.activeReportProfile(reportId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String deactiveReport(String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            return "Empty reportId!";
        }

        try {
            reportService.deactiveReportProfile(reportId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String syncReport(String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            return "Empty reportId!";
        }

        try {
            sdSchedulerService.destroyJob(SdSqlReportBean.KEY_GROUP, reportId);
            sdSchedulerService.scheduleReportJob(reportId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public List<SdReportHistoryData> getFileList(String reportId) {
        String reportPath = getReportPath(reportId);
        if (StringUtils.isEmpty(reportPath)) {
            return null;
        }
        try (Stream<Path> paths = Files.walk(Paths.get(reportPath))) {
            List<SdReportHistoryData> dataList = paths.filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(reportName -> {
                        SdReportHistoryData historyData = new SdReportHistoryData();
                        historyData.setReportId(reportId);
                        historyData.setReportName(reportName);
                        return historyData;
                    }).collect(Collectors.toList());
            return dataList;
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    @Override
    public org.springframework.core.io.Resource downLoadReport(String reportId, String reportName) {
        final String folderPath = getReportPath(reportId);
        if (StringUtils.isEmpty(folderPath)) {
            return null;
        }
        String reportPath = folderPath + File.separator + reportName;
        File file = new File(reportPath);
        if (file.exists()) {
            final String userId = userService.getCurrentSdUserBean().getUserId();
            auditTrailService.insertDownLoadReportAuditTrail(reportName, userId);
            Path path = Paths.get(reportPath);
            try {
                return new UrlResource(path.toUri());
            } catch (MalformedURLException e) {
                LOG.error(e.getMessage());
            }
        }
        return null;
    }

    private String getReportPath(String reportId) {
        SdSqlReportBean bean = reportService.getSqlReportDataByReportId(reportId);
        if (bean == null) {
            return null;
        }

        String exportTo = bean.getExportTo();
        String reportName = bean.getReportName();
        if (StringUtils.isEmpty(exportTo) && StringUtils.isEmpty(reportName)) {
            return null;
        }

        String path = exportTo + File.separator + reportName;
        return path;
    }

    private void checkReportData(RequestReportData data) throws InvalidInputException {
        if (StringUtils.isEmpty(data.getReportName())) {
            throw new InvalidInputException("Empty input report name.");
        }
        if (StringUtils.isEmpty(data.getSql())) {
            throw new InvalidInputException("Empty input SQL statement.");
        }
        if (StringUtils.isEmpty(data.getExportTo())) {
            throw new InvalidInputException("Empty input export place.");
        }
        if (StringUtils.isNotEmpty(data.getEmailTo())) {
            if (!EmailValidator.getInstance().isValid(data.getEmailTo())) {
                throw new InvalidInputException("Please input a valid email.");
            }
        }
        if (StringUtils.isEmpty(data.getCronExp())) {
            throw new InvalidInputException("Empty intput cron expression.");
        } else {
            if (!CronExpression.isValidExpression(data.getCronExp())) {
                throw new InvalidInputException("Invalid cron expression");
            }
        }
    }
}
