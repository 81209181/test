package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.nora.NoraBroadbandInfoData;
import com.hkt.btu.sd.facade.data.nora.NoraDnGroupData;
import com.hkt.btu.sd.facade.data.nora.NorarsBsnData;

public interface NorarsApiFacade {
    NorarsBsnData getBsnByDn(String dn);
    NoraBroadbandInfoData getOfferDetailListByBsn(String bsn);
    NoraDnGroupData getRelatedOfferInfoListByBsn(String bsn);

    String getInventory(String bsn);

    String getServiceAddressByBsn(String bsn);

    String getL1InfoByBsn(String bsn);

    String getOfferDetailListByBsn(String bsn);
}
