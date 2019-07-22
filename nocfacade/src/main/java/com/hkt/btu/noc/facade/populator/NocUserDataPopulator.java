package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import com.hkt.btu.noc.facade.data.NocUserData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NocUserDataPopulator extends AbstractDataPopulator<NocUserData> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Map<String, String> STATUS_DESC_MAP = Map.ofEntries(
            Map.entry("A", "Active"),
            Map.entry("D", "Deactivated"),
            Map.entry("L", "Locked")
    );

    public void populate(NocUserBean source, NocUserData target) {
        target.setUserId(source.getUserId());
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCompanyId(source.getCompanyId());

        // use detailed status
        String detailedStatus = STATUS_DESC_MAP.get(source.getStatus());
        if( StringUtils.isEmpty(detailedStatus) ){
            target.setStatus(source.getStatus());
        } else {
            target.setStatus(detailedStatus);
        }

        // sensitive data
        target.setStaffId(null);
        target.setMobile(null);
        target.setLoginTried(null);
        target.setPasswordModifyDate(null);
        target.setUserGroupList(null);
    }

    public void populate(NocCompanyBean source, NocUserData target) {
        target.setCompanyName(source.getName());
    }

    public void populateSensitiveData(NocUserBean source, NocUserData target){
        target.setStaffId(source.getStaffId());
        target.setMobile(source.getMobile());
        target.setLoginTried(source.getLoginTried());

        LocalDateTime passwordModifydate = source.getPasswordModifydate();
        String formatPwdModifydate = passwordModifydate == null ? null : passwordModifydate.format(DATE_TIME_FORMATTER);
        target.setPasswordModifyDate(formatPwdModifydate);

        Set<GrantedAuthority> authoritySet = source.getAuthorities();
        List<String> userGroupList = new LinkedList<>();
        if( ! CollectionUtils.isEmpty(authoritySet) ) {
            for(GrantedAuthority authority : authoritySet) {
                userGroupList.add(authority.toString());
            }
        }
        target.setUserGroupList(userGroupList);
    }
}