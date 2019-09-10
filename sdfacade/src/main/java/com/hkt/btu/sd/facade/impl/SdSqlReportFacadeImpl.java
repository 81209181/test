package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdSchedulerService;
import com.hkt.btu.sd.core.service.SdSqlReportProfileService;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;
import com.hkt.btu.sd.facade.SdSqlReportFacade;
import com.hkt.btu.sd.facade.data.RequestReportData;
import com.hkt.btu.sd.facade.data.SdSqlReportData;
import com.hkt.btu.sd.facade.populator.SdSqlReportDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
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
        return reportService.getAllReportData(null)
                .stream()
                .map(report -> {
                    SdSqlReportData data = new SdSqlReportData();
                    reportDataPopulator.populate(report, data);
                    return data;
                }).collect(Collectors.toList());
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
    public String createReport(RequestReportData data) {

        return null;
    }

    @Override
    public String deleteReport(String reportId) {
        return null;
    }

    @Override
    public String updateReport(RequestReportData data) {
        return null;
    }


    private void checkReportData(RequestReportData data) {
        Optional.ofNullable(data.getReportName()).orElseThrow(() -> new InvalidInputException("Report Name can't not be null."));
    }
}
