package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.bean.BtuSqlReportBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.impl.BtuSqlReportProfileServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdSqlReportEntity;
import com.hkt.btu.sd.core.dao.mapper.SdSqlReportMapper;
import com.hkt.btu.sd.core.service.SdCronJobLogService;
import com.hkt.btu.sd.core.service.SdSqlReportProfileService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdSqlReportBean;
import com.hkt.btu.sd.core.service.populator.SdReportBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SdSqlReportProfileServiceImpl extends BtuSqlReportProfileServiceImpl implements SdSqlReportProfileService {

    @Resource(name = "reportBeanPopulator")
    SdReportBeanPopulator reportBeanPopulator;

    @Resource(name = "userService")
    SdUserService sdUserService;

    @Resource(name = "cronJobLogService")
    SdCronJobLogService sdCronJobLogService;

    @Resource
    SdSqlReportMapper sdSqlReportMapper;

    private final TransactionTemplate transactionTemplate;

    public SdSqlReportProfileServiceImpl(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public List<BtuSqlReportBean> getAllReportData(String status) {
        List<SdSqlReportEntity> sqlReportData = sdSqlReportMapper.getSqlReportData(null);
        if (CollectionUtils.isEmpty(sqlReportData)) {
            return null;
        }
        List<BtuSqlReportBean> jobProfileBeans = sqlReportData.stream()
                .map(report -> {
                    SdSqlReportBean bean = new SdSqlReportBean();
                    reportBeanPopulator.populate(report, bean);
                    return bean;
                }).collect(Collectors.toList());

        return jobProfileBeans;
    }

    @Override
    public List<SdSqlReportBean> getAllReportBean(String status) {
        List<SdSqlReportEntity> sqlReportData = sdSqlReportMapper.getSqlReportData(status);
        if (CollectionUtils.isEmpty(sqlReportData)) {
            return null;
        }

        List<SdSqlReportBean> reportBeans = sqlReportData.stream().map(report -> {
            SdSqlReportBean bean = new SdSqlReportBean();
            reportBeanPopulator.populate(report, bean);
            return bean;
        }).collect(Collectors.toList());

        return reportBeans;
    }

    @Override
    public List<Map<String, Object>> executeSql(String sql) {
        List<Map<String, Object>> maps = new ArrayList<>();
        if (StringUtils.isNotBlank(sql)) {
            maps = transactionTemplate.execute(txStatus -> {
                txStatus.setRollbackOnly();
                return sdSqlReportMapper.queryBySQL(sql);
            });
        }
        return maps;
    }

    @Override
    public SdSqlReportBean getSqlReportDataByReportId(String reportId) {
        SdSqlReportEntity entity = sdSqlReportMapper.getSqlReportByReportId(reportId);
        SdSqlReportBean bean = new SdSqlReportBean();
        reportBeanPopulator.populate(entity, bean);
        return bean;
    }

    @Override
    @Transactional
    public String createReport(String reportName, String cronExpression, String status,
                               String sql, String exportTo, String emailTo, String remarks) {
        BtuUserBean currentUserBean = sdUserService.getCurrentUserBean();
        String createBy = currentUserBean.getUserId();
        SdSqlReportEntity entity = new SdSqlReportEntity();
        setProperties(reportName, cronExpression, status, sql, exportTo, emailTo, remarks, createBy, entity);
        sdSqlReportMapper.createSqlReport(entity);
        return entity.getReportId();
    }


    @Override
    @Transactional
    public String deleteReport(String reportId) {
        SdSqlReportEntity entity = sdSqlReportMapper.getSqlReportDataByReportId(reportId);
        if (entity == null) {
            return "No such report.";
        }
        sdSqlReportMapper.deleteSqlReportDataByReportId(reportId);
        String reportName = entity.getReportName();

        return reportName;
    }

    @Override
    @Transactional
    public String updateReport(String reportId, String reportName, String cronExpression, String status,
                               String sql, String exportTo, String emailTo, String remarks) {
        BtuUserBean currentUserBean = sdUserService.getCurrentUserBean();
        String modifyBy = currentUserBean.getUserId();
        sdSqlReportMapper.updateReportData(reportId, reportName, sql, cronExpression, exportTo, emailTo, modifyBy, status, remarks);
        return reportName;
    }

    @Override
    @Transactional
    public void activeReportProfile(String reportId) throws InvalidInputException {
        int updateCount = updateRepeortProfileStatus(reportId, SdSqlReportBean.ACTIVE_STATUS);
        if (updateCount < 1) {
            throw new InvalidInputException("Cannot activate job");
        }

        // log
        LOG.info("activate job profile:{},{}", SdSqlReportBean.KEY_GROUP, reportId);
        //sdCronJobLogService.logUserActivateJob(SdSqlReportBean.KEY_GROUP, reportId);
    }

    @Override
    public void deactiveReportProfile(String reportId) throws InvalidInputException {
        int updateCount = updateRepeortProfileStatus(reportId, SdSqlReportBean.DEACTIVE_STATUS);
        if (updateCount < 1) {
            throw new InvalidInputException("Cannot activate job");
        }

        // log
        LOG.info("activate job profile:{},{}", SdSqlReportBean.KEY_GROUP, reportId);
        //sdCronJobLogService.logUserActivateJob(SdSqlReportBean.KEY_GROUP, reportId);
    }

    private int updateRepeortProfileStatus(String reportId, String status) {
        String modifyby = sdUserService.getCurrentUserUserId();
        return sdSqlReportMapper.updateReportStatus(reportId, status, modifyby);
    }

    @Override
    public BtuSqlReportBean getProfileBeanByGrpAndName(String reportId) {
        SdSqlReportEntity entity = sdSqlReportMapper.getSqlReportDataByReportId(reportId);
        if (entity == null) {
            LOG.warn("No such report.");
            return null;
        }
        SdSqlReportBean bean = new SdSqlReportBean();
        reportBeanPopulator.populate(entity, bean);
        return bean;
    }

    private void setProperties(String reportName, String cronExpression, String status, String sql,
                               String exportTo, String emailTo, String remarks, String createBy, SdSqlReportEntity entity) {
        entity.setSql(sql);
        entity.setReportName(reportName);
        entity.setCronExp(cronExpression);
        entity.setEmailTo(emailTo);
        entity.setExportTo(exportTo);
        entity.setRemarks(remarks);
        entity.setCreateby(createBy);
        entity.setStatus(status);
        entity.setModifyby(createBy);
    }
}
