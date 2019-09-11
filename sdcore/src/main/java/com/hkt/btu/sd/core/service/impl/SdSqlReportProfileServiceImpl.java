package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.impl.BtuSqlReportProfileServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdSqlReportEntity;
import com.hkt.btu.sd.core.dao.mapper.SdSqlReportMapper;
import com.hkt.btu.sd.core.service.SdSqlReportProfileService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdCronJobProfileBean;
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

    @Resource
    SdSqlReportMapper sdSqlReportMapper;

    private final TransactionTemplate transactionTemplate;

    public SdSqlReportProfileServiceImpl(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public List<BtuCronJobProfileBean> getAllReportData(String status) {
        List<SdSqlReportEntity> sqlReportData = sdSqlReportMapper.getSqlReportData(status);
        if (CollectionUtils.isEmpty(sqlReportData)) {
            return null;
        }
        List<BtuCronJobProfileBean> jobProfileBeans = sqlReportData.stream()
                .map(report -> {
                    SdSqlReportBean bean = new SdSqlReportBean();
                    reportBeanPopulator.populate(report, bean);
                    return bean;
                }).collect(Collectors.toList())
                .stream()
                .map(reportBean -> {
                    SdCronJobProfileBean target = new SdCronJobProfileBean();
                    reportBeanPopulator.poluate(reportBean, target);
                    return target;
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
        SdSqlReportEntity entity = sdSqlReportMapper.getSqlReportDataByReportId(reportId);
        SdSqlReportBean bean = new SdSqlReportBean();
        reportBeanPopulator.populate(entity, bean);
        return bean;
    }

    @Override
    @Transactional
    public void createReport(String reportName, String cronExpression, String status,
                             String sql, String exportTo, String emailTo, String remarks) {
        BtuUserBean currentUserBean = sdUserService.getCurrentUserBean();
        String createBy = currentUserBean.getUserId();
        sdSqlReportMapper.createSqlReport(reportName, cronExpression, status, sql, exportTo, emailTo, createBy, createBy, remarks);
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
        SdSqlReportEntity entity = sdSqlReportMapper.getSqlReportDataByReportId(null);
        if (entity == null) {
            return "No such report.";
        }
        sdSqlReportMapper.updateSqlReport(reportId, reportName, sql, cronExpression, exportTo, emailTo, modifyBy, status, remarks);
        String report = entity.getReportName();

        return report;
    }

    @Override
    public BtuCronJobProfileBean getProfileBeanByGrpAndName(String keyGroup, String reportId) {
        SdSqlReportEntity entity = sdSqlReportMapper.getSqlReportDataByReportId(reportId);
        if (entity == null) {
            LOG.warn("No such report.");
            return null;
        }
        SdCronJobProfileBean bean = new SdCronJobProfileBean();
        reportBeanPopulator.poluate(entity, bean);
        return bean;
    }
}
