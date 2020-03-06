package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdStatisticService;
import com.hkt.btu.sd.core.service.bean.SdAuditTrailStatisticBean;
import com.hkt.btu.sd.core.service.bean.SdTicketChartBean;
import com.hkt.btu.sd.facade.SdStatisticFacade;
import com.hkt.btu.sd.facade.data.SdAuditTrailStatisticData;
import com.hkt.btu.sd.facade.data.SdStatisticData;
import com.hkt.btu.sd.facade.populator.SdStatisticDataPopulator;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SdStatisticFacadeImpl implements SdStatisticFacade {

    @Resource(name = "statisticService")
    SdStatisticService statisticService;

    @Resource(name = "statisticDataPopulator")
    SdStatisticDataPopulator statisticDataPopulator;

    @Override
    public SdStatisticData getLoginCountLast90Days() {
        List<SdAuditTrailStatisticBean> statisticBeans = statisticService.getLoginCountLast90Days();
        return getSdAuditTrailStatisticData(statisticBeans);
    }

    @Override
    public SdStatisticData getLoginCountLastTwoWeeks() {
        List<SdAuditTrailStatisticBean> statisticBeans = statisticService.getLoginCountLastTwoWeeks();
        return getSdAuditTrailStatisticData(statisticBeans);
    }

    @Override
    public SdStatisticData ticketTypeCountPerOwnerGroup() {
        SdTicketChartBean sdTicketChartBean = statisticService.ticketTypeCountPerOwnerGroup();
        return getSdStatisticData(sdTicketChartBean);
    }

    @Override
    public SdStatisticData ticketStatusCountPerOwnerGroup() {
        SdTicketChartBean sdTicketChartBean = statisticService.ticketStatusCountPerOwnerGroup();
        return getSdStatisticData(sdTicketChartBean);
    }

    private SdStatisticData getSdStatisticData(SdTicketChartBean sdTicketChartBean) {
        if (sdTicketChartBean == null) {
            return null;
        }

        SdStatisticData data = new SdStatisticData();
        data.setMaxTotal(sdTicketChartBean.getMax());
        data.setHeader(sdTicketChartBean.getHeader());
        data.setData(sdTicketChartBean.getStatisticList());
        return data;
    }

    private SdStatisticData<SdAuditTrailStatisticData> getSdAuditTrailStatisticData(List<SdAuditTrailStatisticBean> statisticBeans) {
        if (CollectionUtils.isEmpty(statisticBeans)) {
            return null;
        }

        int maxTotal = statisticBeans.stream().max(Comparator.comparing(SdAuditTrailStatisticBean::getTotal)).get().getTotal();

        List<SdAuditTrailStatisticData> statisticDataList = statisticBeans.stream().map(bean -> {
            SdAuditTrailStatisticData data = new SdAuditTrailStatisticData();
            statisticDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());

        SdStatisticData<SdAuditTrailStatisticData> data = new SdStatisticData<>();
        data.setMaxTotal(maxTotal);
        data.setData(statisticDataList);
        return data;
    }
}
