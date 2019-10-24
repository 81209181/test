package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.nora.NoraDnGroupData;
import com.hkt.btu.sd.facade.data.nora.NorarsBsnData;

public interface NorarsApiFacade {
    NorarsBsnData getBsnByDn(String dn);
    NoraDnGroupData getRelatedOfferInfoListByBsn(String bsn);

    String getInventory(String bsn);
}
