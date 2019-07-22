package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocOperationHistBean;
import com.hkt.btu.noc.facade.data.NocOperationHistData;

public class NocOperationHistDataPopulator extends AbstractDataPopulator<NocOperationHistData> {

    public void populate(NocOperationHistBean source, NocOperationHistData target) {
        target.setLogId(source.getLogId());

        target.setItemType(source.getItemType());
        target.setItemId(source.getItemId());
        target.setDetail(source.getDetail());
        target.setUserId(source.getUserId());

        target.setCreatedate(source.getCreatedate());
    }
}
