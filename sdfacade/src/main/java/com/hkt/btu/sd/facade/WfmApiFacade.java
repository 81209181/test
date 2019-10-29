package com.hkt.btu.sd.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.facade.data.SdPendingOrderData;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.WfmJobDetailsData;
import com.hkt.btu.sd.facade.data.wfm.WfmAppointmentResData;
import com.hkt.btu.sd.facade.data.wfm.WfmJobData;

import java.util.List;

public interface WfmApiFacade {

    Integer createJob(SdTicketData sdTicketData) throws JsonProcessingException;
    boolean closeTicket(Integer ticketMasId);
    WfmJobDetailsData getJobDetails(Integer jobId);

    List<SdServiceTypeOfferMappingBean> getServiceTypeOfferMapping();
    SdPendingOrderData getPendingOrderByBsn(String bsn);

    List<WfmJobData> getJobInfo(Integer ticketMasId);

    WfmAppointmentResData getAppointmentInfo(Integer ticketMasId);
}
