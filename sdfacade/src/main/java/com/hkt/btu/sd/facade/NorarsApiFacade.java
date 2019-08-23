package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.NorarsBsnData;

public interface NorarsApiFacade {
    NorarsBsnData getBsnByDn(String dn);
}
