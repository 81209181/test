package com.hkt.btu.common.facade.populator;

import com.hkt.btu.common.core.service.bean.BaseBean;
import com.hkt.btu.common.facade.data.DataInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractDataPopulator<D extends DataInterface> implements DataPopulator<D> {
    private static final Logger LOG = LogManager.getLogger(AbstractDataPopulator.class);

    @Override
    public void populate(BaseBean source, D target) {
        LOG.debug("AbstractDataPopulator");
    }
}
