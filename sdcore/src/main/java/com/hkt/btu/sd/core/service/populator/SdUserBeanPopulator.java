package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SdUserBeanPopulator extends AbstractBeanPopulator<SdUserBean> {

    public void populate(SdUserEntity source, SdUserBean target) {
        super.populate(source, target);

        // BtuUserBean
        target.setUsername(source.getEmail());
        target.setStatus(source.getStatus());
        target.setPassword(source.getPassword());
        target.setPasswordModifydate(source.getPasswordModifydate());
        target.setLoginTried(source.getLoginTried());
        target.setLdapDomain(source.getLdapDomain());
        // SdUserBean
        target.setUserId(source.getUserId());
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCompanyId(source.getCompanyId());

        // TODO: Encryption function will be done later
        // encrypted data
        target.setLdapDomain(source.getLdapDomain());
        target.setMobile(source.getMobile());
        target.setStaffId(source.getStaffId());
        /*String decryptedMobile = sdSensitiveDataService.decryptToStringSafe(source.getMobile());
        target.setMobile(decryptedMobile);
        // TODO: Encryption function will be done later
        String decryptedStaffId = sdSensitiveDataService.decryptToStringSafe(source.getStaffId());
        target.setStaffId(decryptedStaffId);*/
    }

    public void populate(List<SdUserRoleEntity> userRoleEntityList, SdUserBean target) {
        if (CollectionUtils.isEmpty(userRoleEntityList)) {
            return;
        }

        Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
        for (SdUserRoleEntity userRoleEntity : userRoleEntityList){
            SimpleGrantedAuthority auth = new SimpleGrantedAuthority(userRoleEntity.getRoleId());
            grantedAuthSet.add(auth);
        }

        target.setAuthorities(grantedAuthSet);
    }

}