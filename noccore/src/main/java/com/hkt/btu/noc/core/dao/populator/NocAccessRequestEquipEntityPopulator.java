package com.hkt.btu.noc.core.dao.populator;

import com.hkt.btu.common.core.dao.populator.EntityPopulator;
import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEquipEntity;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestEquipBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;

public class NocAccessRequestEquipEntityPopulator implements EntityPopulator<NocAccessRequestEquipEntity> {

    public void populate(NocAccessRequestEquipBean bean, NocAccessRequestEquipEntity entity) {
        entity.setEquipBrand(bean.getEqBrand());
        entity.setEquipType(bean.getEqType());
        entity.setEquipModel(bean.getEqModel());
        entity.setEquipSerial(bean.getEqSerialNum());

        entity.setEquipRackNum(bean.getEqRackNum());
        entity.setEquipUNum(bean.getEqUNum());
        entity.setAction(bean.getAction());
    }

    public void populate(NocUserBean requesterBean, NocAccessRequestEquipEntity entity) {
        entity.setCreateby(requesterBean.getUserId());
        entity.setModifyby(requesterBean.getUserId());
    }
}
