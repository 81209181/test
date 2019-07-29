package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEquipEntity;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestEquipBean;

public class NocAccessRequestEquipBeanPopulator extends AbstractBeanPopulator<NocAccessRequestEquipBean> {

    // TODO: 2019/7/17  Change Model for Populator
    /*@SuppressWarnings("Duplicates")
    public void populateCreateBean(NocAccessRequestEquipData source, NocAccessRequestEquipBean target) {
        target.setEqBrand(source.getEqBrand());
        target.setEqType(source.getEqType());
        target.setEqModel(source.getEqModel());
        target.setEqSerialNum(source.getEqSerialNum());
        target.setEqRackNum(source.getEqRackNum());
        target.setEqUNum(source.getEqUNum());
        target.setAction(source.getAction());
    }*/

    public void populate(NocAccessRequestEquipEntity source, NocAccessRequestEquipBean target){
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

