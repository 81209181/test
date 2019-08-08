package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdOperationHistEntity;
import com.hkt.btu.sd.core.service.bean.SdOperationHistBean;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class SdOperationHistBeanPopulator extends AbstractBeanPopulator<SdOperationHistBean> {
    private static final Map<String, String> ACCESS_REQUEST_DETAIL_STATUS_MAP = Map.ofEntries(
            Map.entry("Status: null --> A", "Ticket created."),
            Map.entry("Status: A --> C", "Approved to Completed")
    );

    public void populate(SdOperationHistEntity source, SdOperationHistBean target) {
        super.populate(source, target);

        target.setLogId(source.getLogId());
        target.setItemType(source.getItemType());
        target.setItemId(source.getItemId());
        target.setUserId(source.getUserId());

        target.setDetail(source.getDetail());
        if( StringUtils.equals(source.getItemType(), SdOperationHistEntity.ACCESS_REQUEST.ITEM_TYPE) ){
            String detailDesc = ACCESS_REQUEST_DETAIL_STATUS_MAP.get(source.getDetail());
            if( ! StringUtils.isEmpty(detailDesc) ){
                target.setDetail(detailDesc);
            }
        }
    }

}