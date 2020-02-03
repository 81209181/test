package com.hkt.btu.common.facade.impl;


import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.core.service.BtuReportService;
import com.hkt.btu.common.core.service.BtuSchedulerService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuCronJobInstBean;
import com.hkt.btu.common.core.service.bean.BtuReportProfileBean;
import com.hkt.btu.common.core.service.constant.BtuJobStatusEnum;
import com.hkt.btu.common.facade.BtuReportFacade;
import com.hkt.btu.common.facade.data.BtuCronJobInstData;
import com.hkt.btu.common.facade.data.BtuReportFileData;
import com.hkt.btu.common.facade.data.BtuReportProfileData;
import com.hkt.btu.common.facade.data.BtuSimpleResponseData;
import com.hkt.btu.common.facade.populator.BtuCronJobInstDataPopulator;
import com.hkt.btu.common.facade.populator.BtuReportProfileDataPopulator;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BtuReportFacadeImpl implements BtuReportFacade {
    private static final Logger LOG = LogManager.getLogger(BtuReportFacadeImpl.class);

    @Resource(name = "reportService")
    BtuReportService reportService;
    @Resource(name = "schedulerService")
    BtuSchedulerService schedulerService;
    @Resource(name = "auditTrailService")
    BtuAuditTrailService auditTrailService;
    @Resource(name = "userService")
    BtuUserService userService;

    @Resource(name = "reportProfileDataPopulator")
    BtuReportProfileDataPopulator reportProfileDataPopulator;
    @Resource(name = "cronJobInstDataPopulator")
    BtuCronJobInstDataPopulator cronJobInstDataPopulator;

    @Override
    public List<BtuCronJobInstData> getAllReportJobInstance() {
        List<BtuCronJobInstBean> jobBeanInstList;
        try {
            jobBeanInstList = schedulerService.getAllReportJobInstance();
            if (CollectionUtils.isEmpty(jobBeanInstList)) {
                return new ArrayList<>();
            }
        } catch (SchedulerException | ClassCastException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }

        // populate
        List<BtuCronJobInstData> jobDataList = new LinkedList<>();
        for (BtuCronJobInstBean cronJobInstBean : jobBeanInstList) {
            BtuCronJobInstData jobData = new BtuCronJobInstData();
            cronJobInstDataPopulator.populate(cronJobInstBean, jobData);
            jobDataList.add(jobData);
        }
        return jobDataList;
    }

    @Override
    public String resumeReport(String reportName) {
        if (StringUtils.isEmpty(reportName)) {
            return "Empty report name!";
        }

        try {
            schedulerService.resumeJob(BtuReportProfileBean.REPORT_RESERVED_JOB_GROUP, reportName);
        } catch (SchedulerException | InvalidInputException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String pauseReport(String reportName) {
        if (StringUtils.isEmpty(reportName)) {
            return "Empty report name!";
        }

        try {
            schedulerService.pauseJob(BtuReportProfileBean.REPORT_RESERVED_JOB_GROUP, reportName);
        } catch (SchedulerException | InvalidInputException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String triggerReport(String reportName) {
        if (StringUtils.isEmpty(reportName)) {
            return "Empty report name!";
        }

        try {
            schedulerService.triggerJob(BtuReportProfileBean.REPORT_RESERVED_JOB_GROUP, reportName);
        } catch (SchedulerException | InvalidInputException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }


    @Override
    public List<BtuReportProfileData> getAllReportProfiles() {
        return reportService.getAllReportProfiles()
                .stream()
                .map(report -> {
                    BtuReportProfileData data = new BtuReportProfileData();
                    reportProfileDataPopulator.populate(report, data);
                    return data;
                }).collect(Collectors.toList());
    }

    @Override
    public BtuReportProfileData getReportProfileById(String reportId) {
        BtuReportProfileBean bean = reportService.getReportProfileByReportId(reportId);
        BtuReportProfileData data = new BtuReportProfileData();
        reportProfileDataPopulator.populate(bean, data);
        return data;
    }

    @Override
    public BtuSimpleResponseData createReport(BtuReportProfileData data) {
        BtuJobStatusEnum statusEnum = BtuJobStatusEnum.getEnum(data.getStatus());

        try {
            checkReportData(data);
            String reportId = reportService.createReportProfile(data.getReportName(), data.getCronExp(), statusEnum,
                    data.getSql(), data.getEmailTo(), data.getRemarks());
            return BtuSimpleResponseData.of(true, reportId, null);
        } catch (InvalidInputException e) {
            LOG.warn(e.getMessage());
            return BtuSimpleResponseData.of(false, null, e.getMessage());
        }
    }

    @Override
    public BtuSimpleResponseData deleteReportProfile(String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            throw new InvalidInputException("Empty reportId.");
        }

        BtuReportProfileBean reportProfileBean = reportService.getReportProfileByReportId(reportId);
        if(reportProfileBean==null){
            String errorMsg = "Cannot delete report profile. Report profile not found. (reportId=" + reportId + ")";
            LOG.error(errorMsg);
            return BtuSimpleResponseData.of(false, reportId, errorMsg);
        }

        try {
            String deletedReportId = reportService.deleteReportProfile(reportId);
            schedulerService.destroyJob(BtuReportProfileBean.REPORT_RESERVED_JOB_GROUP, reportProfileBean.getReportName());
            return BtuSimpleResponseData.of(true, deletedReportId, null);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            return BtuSimpleResponseData.of(false, reportId, e.getMessage());
        }
    }

    @Override
    public BtuSimpleResponseData updateReportProfile(BtuReportProfileData data) {
        try {
            checkReportData(data);
        } catch (InvalidInputException e) {
            LOG.warn(e.getMessage());
            return BtuSimpleResponseData.of(false, data.getReportId(), e.getMessage());
        }

        BtuJobStatusEnum statusEnum = BtuJobStatusEnum.getEnum(data.getStatus());
        String errorMsg = reportService.updateReportProfile(
                data.getReportId(), data.getReportName(), data.getCronExp(), statusEnum,
                data.getSql(), data.getEmailTo(), data.getRemarks());
        if(StringUtils.isEmpty(errorMsg)){
            return BtuSimpleResponseData.of(true, data.getReportId(), null);
        }else{
            return BtuSimpleResponseData.of(false, data.getReportId(), errorMsg);
        }
    }

    @Override
    public String activateReportProfile(String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            return "Empty reportId!";
        }

        try {
            reportService.activateReportProfile(reportId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String deactivateReportProfile(String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            return "Empty reportId!";
        }

        try {
            reportService.deactivateReportProfile(reportId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String syncReport(String reportName) {
        if (StringUtils.isEmpty(reportName)) {
            return "Empty reportName!";
        }

        try {
            schedulerService.destroyJob(BtuReportProfileBean.REPORT_RESERVED_JOB_GROUP, reportName);

            BtuReportProfileBean reportProfileBean = reportService.getSqlReportProfileByReportName(reportName);
            reportService.scheduleReportJob(reportProfileBean);
            return null;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public List<BtuReportFileData> getFileList(String reportId) {
        Stream<Path> reportPathStream;
        try {
            reportPathStream = reportService.getReportFileList(reportId);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return new ArrayList<>();
        }

        return reportPathStream
                .filter(Files::isRegularFile)
                .sorted(Comparator.comparing(Path::getFileName).reversed())
                .map(Path::getFileName)
                .map(Path::toString)
                .map(reportName -> {
                    BtuReportFileData historyData = new BtuReportFileData();
                    historyData.setReportId(reportId);
                    historyData.setReportName(reportName);
                    return historyData;
                }).collect(Collectors.toList());
    }

    @Override
    public org.springframework.core.io.Resource downloadReportFile(String reportId, String reportFilename) {
        BtuReportProfileBean reportProfileBean = reportService.getReportProfileByReportId(reportId);
        if(reportProfileBean==null){
            LOG.error("Null reportProfileBean.");
            return null;
        }

        final String REPORT_ROOT_DIR = reportService.getReportDirPath(reportProfileBean);
        if (StringUtils.isEmpty(REPORT_ROOT_DIR)) {
            LOG.error("Empty REPORT_ROOT_DIR.");
            return null;
        }

        // check report file exists
        String reportFilePath = REPORT_ROOT_DIR + File.separator + reportFilename;
        File file = new File(reportFilePath);
        if (!file.exists()) {
            LOG.warn("Report file not found. (reportPath={})", reportFilePath);
            return null;
        }

        // add audit
        String userId = userService.getCurrentUserBean().getUserId();
        auditTrailService.insertDownloadReportAuditTrail(reportFilePath, userId);

        // return report file resource
        LOG.info("Getting report file... (reportFilePath={})", reportFilePath);
        try {
            Path path = Paths.get(reportFilePath);
            return new UrlResource(path.toUri());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    private void checkReportData(BtuReportProfileData data) throws InvalidInputException {
        if (StringUtils.isEmpty(data.getReportName())) {
            throw new InvalidInputException("Empty input report name.");
        }else if (StringUtils.isEmpty(data.getSql())) {
            throw new InvalidInputException("Empty input SQL statement.");
        }else if (StringUtils.isNotEmpty(data.getEmailTo())) {
            if (!EmailValidator.getInstance().isValid(data.getEmailTo())) {
                throw new InvalidInputException("Please input a valid email.");
            }
        }else if (StringUtils.isEmpty(data.getCronExp())) {
            throw new InvalidInputException("Empty input cron expression.");
        } else {
            if (!CronExpression.isValidExpression(data.getCronExp())) {
                throw new InvalidInputException("Invalid cron expression");
            }
        }
    }
}
