package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdSymptomGroupEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomGroupRoleMappingEntity;
import com.hkt.btu.sd.core.service.bean.SdSymptomGroupBean;
import org.apache.commons.collections4.CollectionUtils;

import java.util.stream.Collectors;

public class SdSymptomGroupBeanPopulator extends AbstractBeanPopulator<SdSymptomGroupBean> {
    public void pupulate(SdSymptomGroupEntity source, SdSymptomGroupBean target) {
        target.setSymptomGroupCode(source.getSymptomGroupCode());
        target.setSymptomGroupName(source.getSymptomGroupName());
        target.setRoleList(null == source.getRoleList() ?
                null : source.getRoleList().stream().map(SdSymptomGroupRoleMappingEntity::getRoleId).collect(Collectors.toList()));
        target.setCreateby(source.getCreateBy());
        target.setCreatedate(source.getCreateDate());
        target.setModifyby(source.getModifyBy());
        target.setModifydate(source.getCreateDate());
        target.setSymptomCodePrefix(source.getSymptomCodePrefixEntity().getSymtomCodePrefix());
    }
}
