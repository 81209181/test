package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.data.WfmJobDetailsData;
import com.hkt.btu.sd.facade.data.WfmJobInfoResponseData;

import java.util.List;

public interface WfmApiFacade {

    Integer createJob(SdTicketMasData wfmRequestDetailsBeanDate, String createdBy);
    boolean closeTicket(Integer ticketMasId);
    WfmJobDetailsData getJobDetails(Integer jobId);

    List<SdServiceTypeOfferMappingBean> getServiceTypeOfferMapping();
    String getPendingOrder(String serviceNo);

    WfmJobInfoResponseData getJobInfo(Integer ticketMasId);
}
