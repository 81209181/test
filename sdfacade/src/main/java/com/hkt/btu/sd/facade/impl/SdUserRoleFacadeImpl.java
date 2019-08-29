package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import com.hkt.btu.sd.facade.populator.SdUserRoleDataPopulator;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SdUserRoleFacadeImpl implements SdUserRoleFacade {

    @Resource(name = "roleService")
    SdUserRoleService sdUserRoleService;

    @Resource(name = "userRoleDataPopulator")
    SdUserRoleDataPopulator sdUserRoleDataPopulator;

    @Override
    public List<SdUserRoleData> listAllUserRole() {
        List<SdUserRoleData> results = new LinkedList<>();
        List<SdUserRoleBean> allUserRole = sdUserRoleService.getAllUserRole();
        return getSdUserRoleData(results, allUserRole);
    }

    @Override
    public SdUserRoleData getUserRoleByRoleId(String roleId) {
        SdUserRoleData userRoleData = new SdUserRoleData();

        // get user role info
        SdUserRoleBean sdUserRoleBean = sdUserRoleService.getUserRoleByRoleId(roleId);

        if (sdUserRoleBean == null) {
            return null;
        }

        sdUserRoleDataPopulator.populate(sdUserRoleBean, userRoleData);

        return userRoleData;
    }

    @Override
    public List<String> getUserRoleByUserId(String userId) {
        LinkedList<String> results = new LinkedList<>();
        List<SdUserRoleBean> userRoleByUserId = sdUserRoleService.getUserRoleByUserId(userId);

        if (CollectionUtils.isEmpty(userRoleByUserId)) {
            return null;
        }

        for (SdUserRoleBean bean : userRoleByUserId) {
            results.add(bean.getRoleId());
        }

        return results;
    }

    private List<SdUserRoleData> getSdUserRoleData(List<SdUserRoleData> results, List<SdUserRoleBean> userRoleByUserId) {
        if (CollectionUtils.isEmpty(userRoleByUserId)) {
            return null;
        }

        for (SdUserRoleBean bean : userRoleByUserId) {
            SdUserRoleData data = new SdUserRoleData();
            sdUserRoleDataPopulator.populate(bean, data);
            results.add(data);
        }

        return results;
    }

    @Override
    public LinkedList<SdUserRoleData> getEligibleUserRoleList() {
        LinkedList<SdUserRoleData> results = new LinkedList<>();
        List<SdUserRoleBean> eligibleUserGroupGrantList =
                sdUserRoleService.getEligibleUserRoleGrantList();
        results = (LinkedList<SdUserRoleData>) getSdUserRoleData(results, eligibleUserGroupGrantList);
        return results;
    }

    @Override
    public HashMap<String, SdUserRoleData> getUserRoleMap(List<SdUserRoleData> userGroupDataList) {
        HashMap<String, SdUserRoleData> result = new HashMap<>();

        if (CollectionUtils.isEmpty(userGroupDataList)) {
            return result;
        }

        for (SdUserRoleData userRoleData : userGroupDataList) {
            result.put(userRoleData.getRoleDesc(), userRoleData);
        }

        return result;
    }

    @Override
    public Boolean updateUserRole(String roleId, String roleDesc, String status) {
        return sdUserRoleService.updateUserRole(roleId, roleDesc, status);
    }
}
