package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.WfmJobDetailsData;
import com.hkt.btu.sd.facade.data.WfmJobInfoResponseData;

import java.util.List;

public interface WfmApiFacade {

    Integer createJob(SdTicketData sdTicketData, String createdBy);
    boolean closeTicket(Integer ticketMasId);
    WfmJobDetailsData getJobDetails(Integer jobId);

    List<SdServiceTypeOfferMappingBean> getServiceTypeOfferMapping();
    String getPendingOrderByBsn(String bsn);

    WfmJobInfoResponseData getJobInfo(Integer ticketMasId);
}
