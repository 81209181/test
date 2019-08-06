package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdAccessRequestVisitorEntity;
import com.hkt.btu.sd.core.service.SdSensitiveDataService;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestVisitorBean;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;

import javax.annotation.Resource;

public class SdAccessRequestVisitorBeanPopulator extends AbstractBeanPopulator<SdAccessRequestVisitorBean> {

    @Resource(name = "sensitiveDataService")
    SdSensitiveDataService sdSensitiveDataService;

    // TODO: 2019/7/17  Change Model for Populator
   /* public void populateCreateBean(SdAccessRequestVisitorData source, SdAccessRequestVisitorBean target) {
        target.setName(source.getName());
        target.setCompanyName(source.getCompany());
        target.setStaffId(source.getStaffId());
        target.setMobile(source.getMobile());
    }*/


    public void populate(SdAccessRequestVisitorEntity source, SdAccessRequestVisitorBean target){
        super.populate(source, target);

        target.setVisitorAccessId(source.getVisitorAccessId());
        target.setAccessRequestId(source.getVisitorAccessId());
        target.setHashedRequestId(source.getHashedRequestId());

        target.setName(source.getVisitorName());
        target.setCompanyName(source.getCompanyName());
        target.setStaffId( sdSensitiveDataService.decryptToStringSafe(source.getStaffId()) );
        target.setMobile( sdSensitiveDataService.decryptToStringSafe(source.getMobile()) );

        target.setTimeIn(source.getTimeIn()==null ? null : source.getTimeIn().toLocalTime());
        target.setTimeOut(source.getTimeOut()==null ? null : source.getTimeOut().toLocalTime());

        target.setVisitorCardNum(source.getVisitorCardNum());

        target.setVisitDateFrom(source.getVisitDateFrom());
        target.setVisitLocation(source.getVisitLocation());
    }

    public void populate(SdUserBean userBean, SdCompanyBean companyBean, SdAccessRequestVisitorBean target){
        target.setName(userBean.getName());
        target.setCompanyName(companyBean.getName());
        target.setStaffId(userBean.getStaffId());
        target.setMobile(userBean.getMobile());
    }

}