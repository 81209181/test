package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEntity;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestBean;

import javax.annotation.Resource;

public class NocAccessRequestBeanPopulator extends AbstractBeanPopulator<NocAccessRequestBean> {

    @Resource(name = "accessRequestVisitorBeanPopulator")
    NocAccessRequestVisitorBeanPopulator visitorBeanPopulator;

    @Resource(name = "accessRequestEquipBeanPopulator")
    NocAccessRequestEquipBeanPopulator equipBeanPopulator;


    // TODO: 2019/7/17  Change Model for Populator
    /*public void populate(CreateAccessRequestFormData source, NocAccessRequestBean target) {
        target.setAccessRequestId(null);
        target.setHashedRequestId(null);

        target.setVisitReason(source.getVisitReason());
        target.setVisitLocation(source.getVisitLocation());
        target.setVisitRackNum(source.getVisitRackNum());

        target.setVisitDateFrom(LocalDateTime.of(source.getVisitDate(), source.getVisitTimeFrom()));
        target.setVisitDateTo(LocalDateTime.of(source.getVisitDate(), source.getVisitTimeTo()));

        // visitor list
        List<NocAccessRequestVisitorData> visitorDataList = source.getVisitorDataList() == null ? new LinkedList<>() : source.getVisitorDataList();
        List<NocAccessRequestVisitorBean> visitorBeanList = new ArrayList<>();
        target.setRequestVisitorBeanList(visitorBeanList);
        for (NocAccessRequestVisitorData visitorData : visitorDataList) {
            NocAccessRequestVisitorBean visitorBean = new NocAccessRequestVisitorBean();
            visitorBeanPopulator.populateCreateBean(visitorData, visitorBean);
            visitorBeanList.add(visitorBean);
        }

        // equipment list
        List<NocAccessRequestEquipData> equipDataList = source.getEquipDataList() == null ? new LinkedList<>() : source.getEquipDataList();
        List<NocAccessRequestEquipBean> equipBeanList = new ArrayList<>();
        target.setRequestEquipBeanList(equipBeanList);
        for (NocAccessRequestEquipData equipData : equipDataList) {
            NocAccessRequestEquipBean equipBean = new NocAccessRequestEquipBean();
            equipBeanPopulator.populateCreateBean(equipData, equipBean);
            equipBeanList.add(equipBean);
        }
    }*/

    public void populate(NocAccessRequestEntity source, NocAccessRequestBean target) {
        super.populate(source, target);

        target.setAccessRequestId(source.getAccessRequestId());
        target.setHashedRequestId(source.getHashedRequestId());
        target.setStatus(source.getStatus());

        target.setVisitReason(source.getVisitReason());
        target.setVisitLocation(source.getVisitLocation());
        target.setVisitRackNum(source.getVisitRackNum());
        target.setVisitDateFrom(source.getVisitDateFrom());
        target.setVisitDateTo(source.getVisitDateTo());

        target.setVisitorCount(source.getVisitorCount());
    }


}