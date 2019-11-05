package com.hkt.btu.sd.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.WfmJobDetailsData;
import com.hkt.btu.sd.facade.data.wfm.*;

import java.util.List;

public interface WfmApiFacade {

    Integer createJob(SdTicketData sdTicketData) throws JsonProcessingException;
    boolean closeTicket(Integer ticketMasId);
    WfmJobDetailsData getJobDetails(Integer jobId);

    List<SdServiceTypeOfferMappingBean> getServiceTypeOfferMapping();
    WfmPendingOrderData getPendingOrderByBsn(String bsn);

    List<WfmJobData> getJobInfo(Integer ticketMasId);

    WfmAppointmentResData getAppointmentInfo(Integer ticketMasId);

    List<WfmJobProgressData> getJobProgessByTicketId(Integer ticketMasId);

    List<WfmJobRemarksData> getJobRemarkByTicketId(Integer ticketMasId);
}
