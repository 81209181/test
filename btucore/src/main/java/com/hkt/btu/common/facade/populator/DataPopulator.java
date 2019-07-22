package com.hkt.btu.common.facade.populator;

import com.hkt.btu.common.core.service.bean.BaseBean;
import com.hkt.btu.common.facade.data.DataInterface;

public interface DataPopulator <D extends DataInterface> {

    void populate(BaseBean source, D target);

}
