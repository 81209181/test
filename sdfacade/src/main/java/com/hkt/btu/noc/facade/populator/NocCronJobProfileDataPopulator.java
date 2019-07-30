package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocCronJobProfileBean;
import com.hkt.btu.noc.facade.data.NocCronJobInstData;
import com.hkt.btu.noc.facade.data.NocCronJobProfileData;
import org.springframework.stereotype.Component;

@Component
public class NocCronJobProfileDataPopulator extends AbstractDataPopulator<NocCronJobInstData> {

    public void populate(NocCronJobProfileBean source, NocCronJobProfileData target) {
        target.setKeyGroup( source.getKeyGroup() );
        target.setKeyName( source.getKeyName() );
        target.setJobClass( source.getJobClass() );

        target.setActive( source.isActive() );
        target.setMandatory( source.isMandatory() );
        target.setCronExp( source.getCronExp() );
    }
}
