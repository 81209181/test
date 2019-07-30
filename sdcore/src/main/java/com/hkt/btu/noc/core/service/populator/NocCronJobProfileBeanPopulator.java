package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocCronJobEntity;
import com.hkt.btu.noc.core.service.bean.NocCronJobProfileBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class NocCronJobProfileBeanPopulator extends AbstractBeanPopulator<NocCronJobProfileBean> {
    public void populate(NocCronJobEntity source, NocCronJobProfileBean target) {
        super.populate(source, target);

        target.setKeyGroup(source.getJobGroup());
        target.setKeyName(source.getJobName());
        target.setJobClass(source.getJobClass());

        target.setStatus(source.getStatus());
        target.setActive( StringUtils.equals(source.getStatus(), NocCronJobEntity.STATUS.ACTIVE) );
        target.setMandatory( StringUtils.equals(source.getMandatory(), NocCronJobEntity.MANDATORY.YES) );
        target.setCronExp(source.getCronExp());
    }

}