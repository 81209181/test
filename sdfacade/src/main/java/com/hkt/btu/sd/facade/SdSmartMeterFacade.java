package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.BtuSimpleResponseData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.SdTicketData;
import com.hkt.btu.sd.facade.data.SdTicketMasData;
import com.hkt.btu.sd.facade.data.oss.OssCaseData;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SdSmartMeterFacade {
    BtuSimpleResponseData createTicket(Integer poleId, LocalDateTime reportTime, List<String> workingPartyList);
    void notifyCloseTicket(Integer ticketMasId);
    SdTicketData getTicketInfo(Integer ticketMasId);
    PageData<SdTicketMasData> searchTicketList(Pageable pageable, Integer poleId, String plateId,
                                               String createDateFrom, String createDateTo, String ticketType,
                                               String status);
    String translateToSymptom(List<String> workingPartyList);

    BtuSimpleResponseData createTicket(OssCaseData ossCaseData);
}
