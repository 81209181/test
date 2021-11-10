package com.hkt.btu.common.facade.populator;

import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.facade.data.BtuCronJobProfileData;

public class BtuCronJobProfileDataPopulator extends AbstractDataPopulator<BtuCronJobProfileData> {

    public void populate(BtuCronJobProfileBean source, BtuCronJobProfileData target) {
        target.setKeyGroup( source.getKeyGroup() );
        target.setKeyName( source.getKeyName() );
        target.setJobClass( source.getJobClass() );

        target.setActive( source.isActive() );
        target.setMandatory( source.isMandatory() );
        target.setCronExp( source.getCronExp() );
    }
}
