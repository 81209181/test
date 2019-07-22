package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestVisitorBean;
import com.hkt.btu.noc.facade.data.NocAccessRequestVisitorData;

public class NocAccessRequestVisitorDataPopulator extends AbstractDataPopulator<NocAccessRequestVisitorData> {

    public void populate(NocAccessRequestVisitorBean source, NocAccessRequestVisitorData target) {
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

    public void populateSensitiveData(NocAccessRequestVisitorBean source, NocAccessRequestVisitorData target){
        target.setStaffId(source.getStaffId());
        target.setMobile(source.getMobile());
    }
}