package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.dao.entity.NocAccessRequestEntity;
import com.hkt.btu.noc.core.dao.entity.NocUserEntity;
import com.hkt.btu.noc.core.dao.entity.NocUserGroupEntity;
import com.hkt.btu.noc.core.service.NocSensitiveDataService;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NocUserBeanPopulator extends AbstractBeanPopulator<NocUserBean> {

    @Resource(name = "sensitiveDataService")
    NocSensitiveDataService nocSensitiveDataService;

    public void populate(NocUserEntity source, NocUserBean target) {
        super.populate(source, target);

        // BtuUserBean
        target.setUsername(source.getEmail());
        target.setStatus(source.getStatus());
        target.setPassword(source.getPassword());
        target.setPasswordModifydate(source.getPasswordModifydate());
        target.setLoginTried(source.getLoginTried());

        // NocUserBean
        target.setUserId(source.getUserId());
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setCompanyId(source.getCompanyId());

        // encrypted data
        String decryptedMobile = nocSensitiveDataService.decryptToStringSafe(source.getMobile());
        target.setMobile(decryptedMobile);

        String decryptedStaffId = nocSensitiveDataService.decryptToStringSafe(source.getStaffId());
        target.setStaffId(decryptedStaffId);
    }

    public void populate(List<NocUserGroupEntity> userGroupEntityList, NocUserBean target) {
        if(CollectionUtils.isEmpty(userGroupEntityList)){
            return;
        }

        Set<GrantedAuthority> grantedAuthSet = new HashSet<>();
        for (NocUserGroupEntity userGroupEntity : userGroupEntityList){
            SimpleGrantedAuthority auth = new SimpleGrantedAuthority(userGroupEntity.getGroupId());
            grantedAuthSet.add(auth);
        }

        target.setAuthorities(grantedAuthSet);
    }

    public void populate(NocAccessRequestEntity source, NocUserBean target){
        target.setUserId(source.getRequesterId());
        target.setName(source.getRequesterName());
        target.setMobile( nocSensitiveDataService.decryptToStringSafe(source.getMobile()) );
        target.setEmail( nocSensitiveDataService.decryptToStringSafe(source.getEmail()) );
    }

}