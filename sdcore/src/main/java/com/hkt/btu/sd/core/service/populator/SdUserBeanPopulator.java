package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SdUserBeanPopulator extends AbstractBeanPopulator<SdUserBean> {

    public void populate(SdUserEntity source, SdUserBean target) {
        super.populate(source, target);

        String username = Optional.ofNullable(source.getEmail()).orElse(source.getUserId());
        target.setUsername(username);
        // BtuUserBean
        target.setStatus(source.getStatus());
        target.setPassword(source.getPassword());
        target.setPasswordModifydate(source.getPasswordModifydate());
        target.setLoginTried(source.getLoginTried());
        target.setLdapDomain(source.getLdapDomain());
        target.setDomainEmail(source.getDomainEmail());
        // SdUserBean
        target.setUserId(source.getUserId());
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCompanyId(source.getCompanyId());
        target.setLdapDomain(source.getLdapDomain());
        target.setMobile(source.getMobile());
        target.setStaffId(source.getStaffId());
        target.setPrimaryRoleId(source.getPrimaryRoleId());
    }

    public void populate(List<SdUserRoleEntity> userRoleEntityList, SdUserBean target) {
        if (CollectionUtils.isEmpty(userRoleEntityList)) {
            return;
        }

        Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
        for (SdUserRoleEntity userRoleEntity : userRoleEntityList) {
            SimpleGrantedAuthority auth = new SimpleGrantedAuthority(userRoleEntity.getRoleId());
            grantedAuthSet.add(auth);
        }

        target.setAuthorities(grantedAuthSet);
    }


}