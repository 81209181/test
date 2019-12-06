package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.core.exception.ApiException;
import com.hkt.btu.sd.facade.data.ServiceAddressData;
import com.hkt.btu.sd.facade.data.nora.NoraAccountData;
import com.hkt.btu.sd.facade.data.nora.NoraBroadbandInfoData;
import com.hkt.btu.sd.facade.data.nora.NoraDnGroupData;

public interface NorarsApiFacade {
    String getBsnByDn(String dn);

    ServiceAddressData getServiceAddressByBsn(String bsn);
    String getL1InfoByBsn(String bsn);
    String getInventory(String bsn);
    NoraBroadbandInfoData getOfferDetailListByBsn(String bsn);
    NoraDnGroupData getRelatedOfferInfoListByBsn(String bsn);

    NoraAccountData getNGN3OneDayAdminAccount(String bsn) throws ApiException;
}
