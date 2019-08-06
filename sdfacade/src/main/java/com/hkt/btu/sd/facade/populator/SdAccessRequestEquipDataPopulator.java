package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestEquipBean;
import com.hkt.btu.sd.facade.data.SdAccessRequestEquipData;

public class SdAccessRequestEquipDataPopulator extends AbstractDataPopulator<SdAccessRequestEquipData> {

    @SuppressWarnings("Duplicates")
    public void populate(SdAccessRequestEquipBean source, SdAccessRequestEquipData target) {
        String paddedHashedId = source.getHashedRequestId()==null ?
                null : String.format("%07d", source.getHashedRequestId());
        target.setHashedRequestId(paddedHashedId);

        target.setEquipAccessId(source.getEquipAccessId());

        target.setEqBrand(source.getEqBrand());
        target.setEqType(source.getEqType());
        target.setEqModel(source.getEqModel());
        target.setEqSerialNum(source.getEqSerialNum());
        target.setEqRackNum(source.getEqRackNum());
        target.setEqUNum(source.getEqUNum());
        target.setAction(source.getAction());
    }
}