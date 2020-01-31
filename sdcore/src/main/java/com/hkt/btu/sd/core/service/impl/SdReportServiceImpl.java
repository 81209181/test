package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuReportProfileEntity;
import com.hkt.btu.common.core.service.bean.BtuReportProfileBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.constant.BtuJobStatusEnum;
import com.hkt.btu.common.core.service.impl.BtuReportServiceImpl;
import com.hkt.btu.sd.core.dao.mapper.SdSqlReportMapper;
import com.hkt.btu.sd.core.service.SdReportService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdReportProfileBean;
import com.hkt.btu.sd.core.service.populator.SdReportProfileBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SdReportServiceImpl extends BtuReportServiceImpl implements SdReportService {

    @Resource(name = "userService")
    SdUserService sdUserService;

    @Resource(name = "reportProfileBeanPopulator")
    SdReportProfileBeanPopulator reportProfileBeanPopulator;

    @Resource
    SdSqlReportMapper sdSqlReportMapper;

    public SdReportServiceImpl(PlatformTransactionManager transactionManager) {
        super(transactionManager);
    }

    @Override
    protected List<BtuReportProfileBean> getReportProfileInternal(String reportId, String reportName){
        List<BtuReportProfileEntity> sqlReportData = sdSqlReportMapper.getReportProfile(reportId, reportName);
        if (CollectionUtils.isEmpty(sqlReportData)) {
            return null;
        }

        return sqlReportData.stream().map(report -> {
            SdReportProfileBean bean = new SdReportProfileBean();
            reportProfileBeanPopulator.populate(report, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> executeSql(String sql) {
        List<Map<String, Object>> maps = new ArrayList<>();
        if (StringUtils.isNotBlank(sql)) {
            maps = transactionTemplate.execute(txStatus -> {
                txStatus.setRollbackOnly();
                return sdSqlReportMapper.queryWithSql(sql);
            });
        }
        return maps;
    }

    @Override
    @Transactional
    public String createReportProfile(String reportName, String cronExpression, BtuJobStatusEnum status,
                                      String sql, String emailTo, String remarks) {
        BtuUserBean currentUserBean = sdUserService.getCurrentUserBean();
        String createBy = currentUserBean.getUserId();
        BtuReportProfileEntity entity = new BtuReportProfileEntity();
        setProperties(reportName, cronExpression, status, sql, emailTo, remarks, createBy, entity);
        sdSqlReportMapper.createSqlReport(entity);
        return entity.getReportId();
    }


    @Override
    @Transactional
    public String deleteReportProfile(String reportId) {
        BtuReportProfileBean bean = getSqlReportProfileByReportId(reportId);
        if (bean == null) {
            String errorMsg = "Report not found. (reportId=" + reportId + ")";
            LOG.warn(errorMsg);
            return errorMsg;
        }

        int count = sdSqlReportMapper.deleteSqlReportDataByReportId(reportId);
        if(count<1){
            LOG.warn("Cannot delete report. (reportId={})", reportId);
        }
        return bean.getReportId();
    }

    @Override
    @Transactional
    public String updateReportProfile(
            String reportId, String reportName, String cronExpression, BtuJobStatusEnum status,
            String sql, String emailTo, String remarks) {
        BtuUserBean currentUserBean = sdUserService.getCurrentUserBean();
        String modifyBy = currentUserBean.getUserId();

        int count = sdSqlReportMapper.updateReportData(
                reportId, reportName, sql, cronExpression, emailTo, modifyBy, status.getStatusCode(), remarks);
        if(count<1){
            return "Cannot update report profile. (reportId=" + reportId + ")";
        }

        LOG.info("Updated report profile. (reportId={})", reportId);
        return null;
    }

    private void setProperties(String reportName, String cronExpression, BtuJobStatusEnum statusEnum, String sql,
                               String emailTo, String remarks, String createBy, BtuReportProfileEntity entity) {
        entity.setReportName(reportName);
        entity.setCronExp(cronExpression);
        entity.setStatus(statusEnum.getStatusCode());
        entity.setSql(sql);
        entity.setEmailTo(emailTo);
        entity.setRemarks(remarks);
        entity.setCreateby(createBy);
        entity.setModifyby(createBy);
    }
}
