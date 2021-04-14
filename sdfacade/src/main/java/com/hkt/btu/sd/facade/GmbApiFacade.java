package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.gmb.GmbErrorData;
import com.hkt.btu.sd.facade.data.gmb.GmbIddInfoData;
import com.hkt.btu.sd.facade.data.gmb.GmbVehicleData;
import org.springframework.data.domain.Pageable;

public interface GmbApiFacade {

    GmbIddInfoData getIddInfo(String plateId);

    PageData<GmbErrorData> getErrorList(Pageable pageable, String plateId);

    GmbVehicleData getVehicleInfo(String plateId);

    void notifyTicketStatus(String plateNo, Integer ticketId, String time, String action);
}
