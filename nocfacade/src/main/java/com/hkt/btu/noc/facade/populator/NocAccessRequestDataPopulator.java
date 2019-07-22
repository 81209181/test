package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestBean;
import com.hkt.btu.noc.facade.data.NocAccessRequestData;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

public class NocAccessRequestDataPopulator extends AbstractDataPopulator<NocAccessRequestData> {
    private static final Map<String, String> STATUS_DESC_MAP = Map.ofEntries(
            Map.entry("A", "Approved"),
            Map.entry("C", "Completed")
    );


    @Resource(name = "accessRequestVisitorDataPopulator")
    NocAccessRequestVisitorDataPopulator nocAccessRequestVisitorDataPopulator;

    @Resource(name = "accessRequestEquipDataPopulator")
    NocAccessRequestEquipDataPopulator nocAccessRequestEquipDataPopulator;

    public void populate(NocAccessRequestBean source, NocAccessRequestData target) {
        String paddedHashedId = source.getHashedRequestId()==null ?
                null : String.format("%07d", source.getHashedRequestId());
        target.setHashedRequestId(paddedHashedId);

        target.setVisitReason(source.getVisitReason());
        target.setVisitLocation(source.getVisitLocation());
        target.setVisitRackNum(source.getVisitRackNum());
        target.setVisitDate(source.getVisitDateFrom().toLocalDate());
        target.setVisitTimeFrom(source.getVisitDateFrom().toLocalTime());
        target.setVisitTimeTo(source.getVisitDateTo().toLocalTime());

        target.setVisitorCount(source.getVisitorCount());
        target.setCreatedate(source.getCreatedate());

        // detailed status
        String detailedStatus = STATUS_DESC_MAP.get(source.getStatus());
        if( StringUtils.isEmpty(detailedStatus) ){
            target.setStatus(source.getStatus());
        } else {
            target.setStatus(detailedStatus);
        }

        // requester
        if(source.getRequester() != null){
            target.setRequesterId(source.getRequester().getUserId());
            target.setRequesterName(null); // sensitive date
            target.setEmail(null); // sensitive date
            target.setMobile(null); // sensitive date
        }
        if(source.getRequesterCompany() != null){
            target.setCompanyId(source.getRequesterCompany().getCompanyId());
            target.setCompanyName(source.getRequesterCompany().getName());
        }
    }

    public void populateSensitiveData(NocAccessRequestBean source, NocAccessRequestData target) {
        // requester
        if(source.getRequester() != null){
            target.setRequesterName(source.getRequester().getName());
            target.setEmail(source.getRequester().getEmail());
            target.setMobile(source.getRequester().getMobile());
        }
    }


}