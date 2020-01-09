package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.UTCallRequestEntity;
import com.hkt.btu.sd.core.service.bean.UTCallRequestBean;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class UTCallRequestBeanPopulator extends AbstractBeanPopulator<UTCallRequestBean> {
    public void populate(UTCallRequestEntity source, UTCallRequestBean target) {
        super.populate(source, target);

        target.setUTCALLID(source.getUTCALLID());
        target.setBSNNUM(source.getBSNNUM());
        target.setCODE(source.getCODE());
        target.setMSG(source.getMSG());
        target.setSERVICECODE(source.getSERVICECODE());
        target.setSEQ(source.getSEQ());
        target.setSEQTYPE(source.getSEQTYPE());
        target.setLASTCHECKDATE(source.getLASTCHECKDATE());
    }
}