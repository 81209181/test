package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuParamService;
import com.hkt.btu.common.core.service.bean.BtuParamBean;

import java.util.List;

public class BtuParamServiceImpl implements BtuParamService {


    @Override
    public String serialize(Object[] objArray) {
        List<BtuParamBean> paramList;
        // todo [SERVDESK-351]:
        // eg. (String, Integer)
        // only support object type in BtuConfigParamTypeEnum
        // 1. convert to List<BtuParamBean>
        // 2. List<BtuParamBean> to json
        return null;
    }

    @Override
    public Object[] deserialize(String paramListJson) {
//        todo [SERVDESK-351]: reserve serialize
        return new Object[0];
    }
}
