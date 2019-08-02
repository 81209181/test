package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.facade.data.SdCronJobInstData;
import com.hkt.btu.sd.facade.data.SdCronJobProfileData;

public class SdCronJobProfileDataPopulator extends AbstractDataPopulator<SdCronJobInstData> {

    public void populate(BtuCronJobProfileBean source, SdCronJobProfileData target) {
        target.setKeyGroup( source.getKeyGroup() );
        target.setKeyName( source.getKeyName() );
        target.setJobClass( source.getJobClass() );

        target.setActive( source.isActive() );
        target.setMandatory( source.isMandatory() );
        target.setCronExp( source.getCronExp() );
    }
}
