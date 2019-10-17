package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.facade.data.*;
import org.apache.commons.collections4.CollectionUtils;

public class BesFaultInfoDataPopulator extends AbstractDataPopulator<BesFaultInfoData> {

    public void populate(SdTicketData source, BesFaultInfoData target) {
        target.setRepeatedSubscriberIdCount(0);
        target.setRepeatedGroupIdCount(0);

        SdTicketMasData sdTicketMasData = source.getTicketMasInfo();
        if(sdTicketMasData!=null){
            target.setRequestId(sdTicketMasData.getTicketMasId());
            target.setCustName(sdTicketMasData.getCustCode());
            target.setSubFaultStatus(sdTicketMasData.getStatus());

            target.setCreatedBy(sdTicketMasData.getCreateBy());
            target.setCreatedDate(sdTicketMasData.getCreateDate().toString());
            target.setLastUpdatedBy(sdTicketMasData.getModifyBy());
            target.setLastUpdatedDate(sdTicketMasData.getModifyDate().toString());
            target.setClosedDate(sdTicketMasData.getModifyDate().toString()); // todo: to-be-updated later when TICKET_MAS has new column
        }

        SdTicketServiceData sdTicketServiceData = CollectionUtils.isEmpty(source.getServiceInfo()) ?
                null : source.getServiceInfo().get(0);
        if(sdTicketServiceData!=null){
            target.setProductType(sdTicketServiceData.getServiceType());
            target.setFaultId(sdTicketServiceData.getTicketDetId().toString());
//            target.setSubFaultId(sdTicketServiceData.getTicketDetId().toString());

            SdSymptomData sdSymptomData = CollectionUtils.isEmpty(sdTicketServiceData.getFaultsList()) ?
                    null : sdTicketServiceData.getFaultsList().get(0);
            if(sdSymptomData!=null){
                target.setMainFaultCode(sdSymptomData.getSymptomGroupCode());
                target.setSubFaultCode(sdSymptomData.getSymptomCode());
            }
        }
    }
}
