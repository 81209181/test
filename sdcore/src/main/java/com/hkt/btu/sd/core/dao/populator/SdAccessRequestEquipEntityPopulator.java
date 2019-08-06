package com.hkt.btu.sd.core.dao.populator;

import com.hkt.btu.common.core.dao.populator.EntityPopulator;
import com.hkt.btu.sd.core.dao.entity.SdAccessRequestEquipEntity;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestEquipBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;

public class SdAccessRequestEquipEntityPopulator implements EntityPopulator<SdAccessRequestEquipEntity> {

    public void populate(SdAccessRequestEquipBean bean, SdAccessRequestEquipEntity entity) {
        entity.setEquipBrand(bean.getEqBrand());
        entity.setEquipType(bean.getEqType());
        entity.setEquipModel(bean.getEqModel());
        entity.setEquipSerial(bean.getEqSerialNum());

        entity.setEquipRackNum(bean.getEqRackNum());
        entity.setEquipUNum(bean.getEqUNum());
        entity.setAction(bean.getAction());
    }

    public void populate(SdUserBean requesterBean, SdAccessRequestEquipEntity entity) {
        entity.setCreateby(requesterBean.getUserId());
        entity.setModifyby(requesterBean.getUserId());
    }
}
