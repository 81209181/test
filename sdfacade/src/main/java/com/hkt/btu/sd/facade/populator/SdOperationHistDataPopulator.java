package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdOperationHistBean;
import com.hkt.btu.sd.facade.data.SdOperationHistData;

public class SdOperationHistDataPopulator extends AbstractDataPopulator<SdOperationHistData> {

    public void populate(SdOperationHistBean source, SdOperationHistData target) {
        target.setLogId(source.getLogId());

        target.setItemType(source.getItemType());
        target.setItemId(source.getItemId());
        target.setDetail(source.getDetail());
        target.setUserId(source.getUserId());

        target.setCreatedate(source.getCreatedate());
    }
}
