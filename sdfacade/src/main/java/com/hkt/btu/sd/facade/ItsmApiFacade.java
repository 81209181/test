package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.itsm.ItsmSearchProfileResponseData;
import com.hkt.btu.sd.facade.data.itsm.ItsmTenantData;

public interface ItsmApiFacade {
    ItsmSearchProfileResponseData searchProfileByCustName(String custName);
    ItsmSearchProfileResponseData searchProfileByCustId(String custId);
    ItsmSearchProfileResponseData searchProfileByServiceNo(String serviceNo);
    ItsmSearchProfileResponseData searchProfileByTenantId(String tenantId);
    ItsmSearchProfileResponseData searchProfileByDomainName(String domainName);

    ItsmTenantData getTenant(int tenantId);
}
