package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdAccessRequestEquipEntity;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestEquipBean;

public class SdAccessRequestEquipBeanPopulator extends AbstractBeanPopulator<SdAccessRequestEquipBean> {

    // TODO: 2019/7/17  Change Model for Populator
    /*@SuppressWarnings("Duplicates")
    public void populateCreateBean(SdAccessRequestEquipData source, SdAccessRequestEquipBean target) {
        target.setEqBrand(source.getEqBrand());
        target.setEqType(source.getEqType());
        target.setEqModel(source.getEqModel());
        target.setEqSerialNum(source.getEqSerialNum());
        target.setEqRackNum(source.getEqRackNum());
        target.setEqUNum(source.getEqUNum());
        target.setAction(source.getAction());
    }*/

    public void populate(SdAccessRequestEquipEntity source, SdAccessRequestEquipBean target){
        super.populate(source, target);

        target.setEquipAccessId(source.getEquipAccessId());
        target.setAccessRequestId(source.getAccessRequestId());
        target.setHashedRequestId(source.getHashedRequestId());

        target.setEqBrand(source.getEquipBrand());
        target.setEqType(source.getEquipType());
        target.setEqModel(source.getEquipModel());
        target.setEqSerialNum(source.getEquipSerial());
        target.setEqRackNum(source.getEquipRackNum());
        target.setEqUNum(source.getEquipUNum());
        target.setAction(source.getAction());
    }

}

