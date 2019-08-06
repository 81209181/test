package com.hkt.btu.common.core.service.populator;


import com.hkt.btu.common.core.dao.entity.BtuUserEntity;
import com.hkt.btu.common.core.dao.entity.BtuUserGroupEntity;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BtuUserBeanPopulator extends AbstractBeanPopulator<BtuUserBean> {

    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;

    public void populate(BtuUserEntity source, BtuUserBean target) {
        super.populate(source, target);

        // BtuUserBean
        target.setUsername(source.getEmail());
        target.setStatus(source.getStatus());
        target.setPassword(source.getPassword());
        target.setPasswordModifydate(source.getPasswordModifydate());
        target.setLoginTried(source.getLoginTried());

        // SdUserBean
        target.setUserId(source.getUserId());
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCompanyId(source.getCompanyId());

        // encrypted data
        String decryptedMobile = sensitiveDataService.decryptToStringSafe(source.getMobile());
        target.setMobile(decryptedMobile);

        String decryptedStaffId = sensitiveDataService.decryptToStringSafe(source.getStaffId());
        target.setStaffId(decryptedStaffId);
    }

    public void populate(List<BtuUserGroupEntity> userGroupEntityList, BtuUserBean target) {
        if(CollectionUtils.isEmpty(userGroupEntityList)){
            return;
        }

        Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
        for (BtuUserGroupEntity userGroupEntity : userGroupEntityList){
            SimpleGrantedAuthority auth = new SimpleGrantedAuthority(userGroupEntity.getGroupId());
            grantedAuthSet.add(auth);
        }

        target.setAuthorities(grantedAuthSet);
    }

}