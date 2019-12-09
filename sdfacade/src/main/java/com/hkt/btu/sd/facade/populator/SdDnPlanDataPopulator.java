package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.facade.constant.DnPlanActionEnum;
import com.hkt.btu.sd.facade.data.SdCronJobInstData;
import com.hkt.btu.sd.facade.data.SdDnPlanData;
import com.hkt.btu.sd.facade.data.nora.NoraDnPlanData;

public class SdDnPlanDataPopulator extends AbstractDataPopulator<SdCronJobInstData> {

    public void populate(NoraDnPlanData source, SdDnPlanData target) {
        target.setParam(source.getParam());
        target.setPlan(source.getPlan());

        DnPlanActionEnum dnPlanActionEnum = DnPlanActionEnum.getEnum(source.getAction());
        target.setAction(dnPlanActionEnum);
    }
}
