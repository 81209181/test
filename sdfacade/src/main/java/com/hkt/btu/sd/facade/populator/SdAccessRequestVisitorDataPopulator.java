package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestVisitorBean;
import com.hkt.btu.sd.facade.data.SdAccessRequestVisitorData;

public class SdAccessRequestVisitorDataPopulator extends AbstractDataPopulator<SdAccessRequestVisitorData> {

    public void populate(SdAccessRequestVisitorBean source, SdAccessRequestVisitorData target) {
        String paddedHashedId = source.getHashedRequestId()==null ?
                null : String.format("%07d", source.getHashedRequestId());
        target.setHashedRequestId(paddedHashedId);

        target.setVisitorAccessId(source.getVisitorAccessId());

        target.setName(source.getName());
        target.setCompany(source.getCompanyName());

        target.setTimeIn(source.getTimeIn());
        target.setTimeOut(source.getTimeOut());

        target.setVisitorCardNum(source.getVisitorCardNum());

        target.setVisitDate( source.getVisitDateFrom()==null ? null : source.getVisitDateFrom().toLocalDate() );
        target.setVisitLocation(source.getVisitLocation());

        // sensitive data
        target.setStaffId( null );
        target.setMobile( null );

    }

    public void populateSensitiveData(SdAccessRequestVisitorBean source, SdAccessRequestVisitorData target){
        target.setStaffId(source.getStaffId());
        target.setMobile(source.getMobile());
    }
}