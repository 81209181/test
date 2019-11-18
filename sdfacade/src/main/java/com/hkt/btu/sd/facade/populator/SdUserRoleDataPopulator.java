package com.hkt.btu.sd.facade.populator;

import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class SdUserRoleDataPopulator extends AbstractDataPopulator<SdUserRoleData> {

    private static final Map<String, String> STATUS_DESC_MAP = Map.ofEntries(
            Map.entry("A", "Active"),
            Map.entry("D", "Deactivated"),
            Map.entry("L", "Locked")
    );

    public void populate(SdUserRoleBean source, SdUserRoleData target) {
        target.setRoleId(source.getRoleId());
        target.setRoleDesc(source.getRoleDesc());
        target.setParentRoleId(source.getParentRoleId());
        target.setAbstractFlag(Boolean.toString(source.isAbstract()));
        target.setPrimaryRole(source.isPrimaryRole());

        String detailedStatus = STATUS_DESC_MAP.get(source.getStatus());
        if (StringUtils.isEmpty(detailedStatus)) {
            target.setStatus(source.getStatus());
        } else {
            target.setStatus(detailedStatus);
        }
    }
}
