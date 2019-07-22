package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocAccessRequestVisitorEntity;
import com.hkt.btu.noc.core.service.NocSensitiveDataService;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestVisitorBean;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;

import javax.annotation.Resource;

public class NocAccessRequestVisitorBeanPopulator extends AbstractBeanPopulator<NocAccessRequestVisitorBean> {

    @Resource(name = "sensitiveDataService")
    NocSensitiveDataService nocSensitiveDataService;

    // TODO: 2019/7/17  Change Model for Populator
   /* public void populateCreateBean(NocAccessRequestVisitorData source, NocAccessRequestVisitorBean target) {
        target.setName(source.getName());
        target.setCompanyName(source.getCompany());
        target.setStaffId(source.getStaffId());
        target.setMobile(source.getMobile());
    }*/


    public void populate(NocAccessRequestVisitorEntity source, NocAccessRequestVisitorBean target){
        super.populate(source, target);

        target.setVisitorAccessId(source.getVisitorAccessId());
        target.setAccessRequestId(source.getVisitorAccessId());
        target.setHashedRequestId(source.getHashedRequestId());

        target.setName(source.getVisitorName());
        target.setCompanyName(source.getCompanyName());
        target.setStaffId( nocSensitiveDataService.decryptToStringSafe(source.getStaffId()) );
        target.setMobile( nocSensitiveDataService.decryptToStringSafe(source.getMobile()) );

        target.setTimeIn(source.getTimeIn()==null ? null : source.getTimeIn().toLocalTime());
        target.setTimeOut(source.getTimeOut()==null ? null : source.getTimeOut().toLocalTime());

        target.setVisitorCardNum(source.getVisitorCardNum());

        target.setVisitDateFrom(source.getVisitDateFrom());
        target.setVisitLocation(source.getVisitLocation());
    }

    public void populate(NocUserBean userBean, NocCompanyBean companyBean, NocAccessRequestVisitorBean target){
        target.setName(userBean.getName());
        target.setCompanyName(companyBean.getName());
        target.setStaffId(userBean.getStaffId());
        target.setMobile(userBean.getMobile());
    }

}