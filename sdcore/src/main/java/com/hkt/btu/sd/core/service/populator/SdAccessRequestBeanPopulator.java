package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdAccessRequestEntity;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestBean;

import javax.annotation.Resource;

public class SdAccessRequestBeanPopulator extends AbstractBeanPopulator<SdAccessRequestBean> {

    @Resource(name = "accessRequestVisitorBeanPopulator")
    SdAccessRequestVisitorBeanPopulator visitorBeanPopulator;

    @Resource(name = "accessRequestEquipBeanPopulator")
    SdAccessRequestEquipBeanPopulator equipBeanPopulator;


    // TODO: 2019/7/17  Change Model for Populator
    /*public void populate(CreateAccessRequestFormData source, SdAccessRequestBean target) {
        target.setAccessRequestId(null);
        target.setHashedRequestId(null);

        target.setVisitReason(source.getVisitReason());
        target.setVisitLocation(source.getVisitLocation());
        target.setVisitRackNum(source.getVisitRackNum());

        target.setVisitDateFrom(LocalDateTime.of(source.getVisitDate(), source.getVisitTimeFrom()));
        target.setVisitDateTo(LocalDateTime.of(source.getVisitDate(), source.getVisitTimeTo()));

        // visitor list
        List<SdAccessRequestVisitorData> visitorDataList = source.getVisitorDataList() == null ? new LinkedList<>() : source.getVisitorDataList();
        List<SdAccessRequestVisitorBean> visitorBeanList = new ArrayList<>();
        target.setRequestVisitorBeanList(visitorBeanList);
        for (SdAccessRequestVisitorData visitorData : visitorDataList) {
            SdAccessRequestVisitorBean visitorBean = new SdAccessRequestVisitorBean();
            visitorBeanPopulator.populateCreateBean(visitorData, visitorBean);
            visitorBeanList.add(visitorBean);
        }

        // equipment list
        List<SdAccessRequestEquipData> equipDataList = source.getEquipDataList() == null ? new LinkedList<>() : source.getEquipDataList();
        List<SdAccessRequestEquipBean> equipBeanList = new ArrayList<>();
        target.setRequestEquipBeanList(equipBeanList);
        for (SdAccessRequestEquipData equipData : equipDataList) {
            SdAccessRequestEquipBean equipBean = new SdAccessRequestEquipBean();
            equipBeanPopulator.populateCreateBean(equipData, equipBean);
            equipBeanList.add(equipBean);
        }
    }*/

    public void populate(SdAccessRequestEntity source, SdAccessRequestBean target) {
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