package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdSchedulerService;
import com.hkt.btu.sd.core.service.SdSqlReportProfileService;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;
import com.hkt.btu.sd.facade.SdSqlReportFacade;
import com.hkt.btu.sd.facade.data.RequestReportData;
import com.hkt.btu.sd.facade.data.ResponseReportData;
import com.hkt.btu.sd.facade.data.SdSqlReportData;
import com.hkt.btu.sd.facade.populator.SdSqlReportDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class SdSqlReportFacadeImpl implements SdSqlReportFacade {

    private static final Logger LOG = LogManager.getLogger(SdSqlReportFacadeImpl.class);

    @Resource(name = "reportDataPopulator")
    SdSqlReportDataPopulator reportDataPopulator;

    @Resource(name = "sqlReportProfileService")
    SdSqlReportProfileService reportService;

    @Resource(name = "schedulerService")
    SdSchedulerService sdSchedulerService;

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
        try {
            checkReportData(data);
            reportService.createReport(data.getReportName(), data.getCronExpression(), data.getStatus(),
                    data.getSql(), data.getExportTo(), data.getEmailTo(), data.getRemarks());
            sdSchedulerService.scheduleReportJob(data.getReportName());
        } catch (InvalidInputException | SchedulerException | ClassNotFoundException e) {
            LOG.warn(e.getMessage());
            return ResponseReportData.of(null, e.getMessage());
        }
        return ResponseReportData.of(data.getReportId(), null);
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
        }
        return null;
    }

    @Override
    public ResponseReportData updateReport(RequestReportData data) {
        try {
            checkReportData(data);
            String reportName = reportService.updateReport(data.getReportId(), data.getReportName(), data.getCronExpression(), data.getStatus(),
                    data.getSql(), data.getExportTo(), data.getEmailTo(), data.getRemarks());
            sdSchedulerService.pauseJob(SdSqlReportBean.KEY_GROUP, reportName);
            sdSchedulerService.destroyJob(SdSqlReportBean.KEY_GROUP, reportName);
            sdSchedulerService.scheduleReportJob(reportName);
        } catch (InvalidInputException | SchedulerException | ClassNotFoundException e) {
            LOG.warn(e.getMessage());
            return ResponseReportData.of(null, e.getMessage());
        }
        return ResponseReportData.of(data.getReportId(), null);
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
        if (StringUtils.isEmpty(data.getEmailTo())) {
            throw new InvalidInputException("Empty input email.");
        }
        if (StringUtils.isEmpty(data.getCronExpression())) {
            throw new InvalidInputException("Empty int cron expression.");
        }
    }
}
