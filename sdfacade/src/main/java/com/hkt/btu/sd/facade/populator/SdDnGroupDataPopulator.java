package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.facade.data.SdCronJobInstData;
import com.hkt.btu.sd.facade.data.SdDnGroupData;
import com.hkt.btu.sd.facade.data.norars.NoraDnGroupData;

public class SdDnGroupDataPopulator extends AbstractDataPopulator<SdCronJobInstData> {

    public void populate(NoraDnGroupData source, SdDnGroupData target) {
        target.setAsCluster(source.getAsCluster());
        target.setAdminPortalId(source.getAdminPortalId());
        target.setCliNumber(source.getCliNumber());
        target.setRecordingProvisionMethod(source.getRecordingProvisionMethod());
        target.setDod(source.getDod());
        target.setCac(source.getCac());
    }
}
