package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdAuditTrailStatisticEntity;
import com.hkt.btu.sd.core.dao.entity.SdTicketStatisticEntity;
import com.hkt.btu.sd.core.dao.mapper.SdAuditTrailMapper;
import com.hkt.btu.sd.core.dao.mapper.SdTicketMasMapper;
import com.hkt.btu.sd.core.service.SdStatisticService;
import com.hkt.btu.sd.core.service.bean.SdAuditTrailStatisticBean;
import com.hkt.btu.sd.core.service.bean.SdTicketChartBean;
import com.hkt.btu.sd.core.service.populator.SdStatisticBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SdStatisticServiceImpl implements SdStatisticService {

    private static final String TICKET_STATISTIC_FILTER = "()";

    @Resource
    SdAuditTrailMapper auditTrailMapper;

    @Resource
    SdTicketMasMapper ticketMasMapper;

    @Resource(name = "statisticBeanPopulator")
    SdStatisticBeanPopulator statisticBeanPopulator;

    @Override
    public List<SdAuditTrailStatisticBean> getLoginCountLast90Days() {
        List<SdAuditTrailStatisticEntity> statisticEntityList = auditTrailMapper.getLoginCountLast90Days();
        List<SdAuditTrailStatisticBean> beanList = populate(statisticEntityList);
        return beanList;
    }

    @Override
    public List<SdAuditTrailStatisticBean> getLoginCountLastTwoWeeks() {
        List<SdAuditTrailStatisticEntity> statisticEntityList = auditTrailMapper.getLoginCountLastTwoWeeks();
        List<SdAuditTrailStatisticBean> beanList = populate(statisticEntityList);
        return beanList;
    }

    @Override
    public SdTicketChartBean ticketTypeCountPerOwnerGroup() {
        List<SdTicketStatisticEntity> ticketTypeList = ticketMasMapper.ticketTypeCountPerOwnerGroup();
        return getSdTicketChartBean(ticketTypeList);
    }

    @Override
    public SdTicketChartBean ticketStatusCountPerOwnerGroup() {
        List<SdTicketStatisticEntity> ticketStatusList = ticketMasMapper.ticketStatusCountPerOwnerGroup();
        return getSdTicketChartBean(ticketStatusList);
    }

    private SdTicketChartBean getSdTicketChartBean(List<SdTicketStatisticEntity> ticketStatusList) {
        List<String> header = getHeader(ticketStatusList, TICKET_STATISTIC_FILTER);
        if (CollectionUtils.isEmpty(header)) {
            return null;
        }

        List<String[]> statisticList = getStatisticList(header, ticketStatusList);
        int maxCount = ticketStatusList.stream().max(Comparator.comparing(SdTicketStatisticEntity::getTotalCount)).get().getTotalCount();
        SdTicketChartBean bean = new SdTicketChartBean();
        bean.setMax(maxCount);
        bean.setHeader(header);
        bean.setStatisticList(statisticList);
        return bean;
    }

    private List<String> getHeader(List<SdTicketStatisticEntity> statisticList, String filter) {
        if (CollectionUtils.isEmpty(statisticList)) {
            return null;
        }

        List<String> title = new ArrayList<>(10);
        title.add("Days");
        List<String> titles = statisticList.stream()
                .map(SdTicketStatisticEntity::getStatistics)
                .distinct()
                .filter(owningRole-> owningRole != null && !filter.equals(owningRole))
                .collect(Collectors.toList());
        title.addAll(titles);
        return title;
    }

    private List<String[]> getStatisticList(List<String> headerList, List<SdTicketStatisticEntity> ticketStatisticEntityList) {
        if (CollectionUtils.isEmpty(headerList) || CollectionUtils.isEmpty(ticketStatisticEntityList)) {
            return null;
        }

        List<String[]> ticketStatisticList = new ArrayList<>(10);
        String flag = "";

        for (SdTicketStatisticEntity entity : ticketStatisticEntityList) {
            if (flag.equals(entity.getStatisticDate())) {
                continue;
            }

            List<SdTicketStatisticEntity> sameStatisticDateList = getSameDateTicketStatisticList(ticketStatisticEntityList, entity);
            if (CollectionUtils.isEmpty(sameStatisticDateList)) {
                continue;
            }

            String[] ticketStatistics = adjustStatisticData(sameStatisticDateList, headerList);
            ticketStatistics[0] = entity.getStatisticDate();
            flag = entity.getStatisticDate();
            ticketStatisticList.add(ticketStatistics);
        }
        return ticketStatisticList;
    }

    private List<SdTicketStatisticEntity> getSameDateTicketStatisticList(List<SdTicketStatisticEntity> ticketStatisticEntityList, SdTicketStatisticEntity entity) {
        List<SdTicketStatisticEntity> sameStatisticDateList = new ArrayList<>(10);
        for (SdTicketStatisticEntity header : ticketStatisticEntityList) {
            if (entity.getStatisticDate().equals(header.getStatisticDate())) {
                sameStatisticDateList.add(header);
            }
        }
        return sameStatisticDateList;
    }

    private String[] adjustStatisticData(List<SdTicketStatisticEntity> sameStatisticDateList, List<String> headerList) {
        String [] ticketStatistics = new String[headerList.size()];
        if (CollectionUtils.isEmpty(sameStatisticDateList)) {
            return null;
        }
        for (SdTicketStatisticEntity statisticEntity : sameStatisticDateList) {
            int index;
            if (headerList.contains(statisticEntity.getStatistics())) {
                index = headerList.indexOf(statisticEntity.getStatistics());
                ticketStatistics[index] = String.valueOf(statisticEntity.getTotalCount());
            } else {
                continue;
            }
        }
        return ticketStatistics;
    }

    private List<SdAuditTrailStatisticBean> populate(List<SdAuditTrailStatisticEntity> statisticEntityList) {
        if (CollectionUtils.isEmpty(statisticEntityList)) {
            return null;
        }

        List<SdAuditTrailStatisticBean> statisticBeans = statisticEntityList.stream().map(entity -> {
            SdAuditTrailStatisticBean bean = new SdAuditTrailStatisticBean();
            statisticBeanPopulator.populate(bean, entity);
            return bean;
        }).collect(Collectors.toList());

        return statisticBeans;
    }
}
