package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.NorarsBsnData;

public interface NorarsApiFacade {
    NorarsBsnData getBsnByDn(String dn);

    String getInventory(String bsn);

    String getServiceAddressByBsn(String bsn);

    String getL1InfoByBsn(String bsn);

    String getOfferDetailListByBsn(String bsn);
}
