package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.SdStatisticData;

public interface SdStatisticFacade {

    SdStatisticData getLoginCountLast90Days();

    SdStatisticData getLoginCountLastTwoWeeks();

    SdStatisticData ticketTypeCountPerOwnerGroup();

    SdStatisticData ticketStatusCountPerOwnerGroup();
}
