package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestEquipBean;
import com.hkt.btu.noc.facade.data.NocAccessRequestEquipData;

public class NocAccessRequestEquipDataPopulator extends AbstractDataPopulator<NocAccessRequestEquipData> {

    @SuppressWarnings("Duplicates")
    public void populate(NocAccessRequestEquipBean source, NocAccessRequestEquipData target) {
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