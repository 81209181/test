package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.data.WfmJobDetailsData;
import com.hkt.btu.sd.facade.data.WfmJobInfoResponseData;
import com.hkt.btu.sd.facade.data.WfmPendingOrderData;

import java.util.List;

public interface WfmApiFacade {

    Integer createJob(SdTicketMasData wfmRequestDetailsBeanDate, String createdBy);
    Integer completeJob(Integer jobId, String completeDateTime, String remark, String createdBy);
    WfmJobDetailsData getJobDetails(Integer jobId);

    List<SdServiceTypeOfferMappingBean> getServiceTypeOfferMapping();
    WfmPendingOrderData getPendingOrderByBsn(String bsn);

    WfmJobInfoResponseData getJobInfo(Integer ticketMasId);
}
