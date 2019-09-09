package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.SdPathCtrlService;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.core.service.bean.SdUserRolePathCtrlBean;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdUserPathCtrlData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import com.hkt.btu.sd.facade.populator.SdUserRoleDataPopulator;
import com.hkt.btu.sd.facade.populator.SdUserRolePathCtrlPopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class SdUserRoleFacadeImpl implements SdUserRoleFacade {

    @Resource(name = "roleService")
    SdUserRoleService sdUserRoleService;

    @Resource(name = "pathCtrlService")
    SdPathCtrlService sdPathCtrlService;

    @Resource(name = "userRoleDataPopulator")
    SdUserRoleDataPopulator sdUserRoleDataPopulator;

    @Resource(name = "userRolePathCtrlDataPopulator")
    SdUserRolePathCtrlPopulator sdUserRolePathCtrlPopulator;

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
    public EditResultData getUserRoleByUserId(String userId) {
        List<String> results = new ArrayList<>();
        try {
            List<SdUserRoleBean> userRoleBeanList = sdUserRoleService.getUserRoleByUserId(userId);
            if (CollectionUtils.isEmpty(userRoleBeanList)) {
                return EditResultData.error("This user no role.");
            }
            results = userRoleBeanList.stream().map(bean -> {
                SdUserRoleData data = new SdUserRoleData();
                sdUserRoleDataPopulator.populate(bean, data);
                return data;
            }).collect(Collectors.toList())
                    .stream()
                    .map(SdUserRoleData::getRoleId)
                    .collect(Collectors.toList());
        } catch (InsufficientAuthorityException e) {
            return EditResultData.error(e.getMessage());
        }

        return EditResultData.dataList(results);
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
        HashMap<String, SdUserRoleData> result = new HashMap<>(16);

        if (CollectionUtils.isEmpty(userGroupDataList)) {
            return result;
        }

        for (SdUserRoleData userRoleData : userGroupDataList) {
            result.put(userRoleData.getRoleDesc(), userRoleData);
        }

        return result;
    }

    @Override
    public String updateUserRole(String roleId, String roleDesc, String status) {
        if (StringUtils.isEmpty(roleId)) {
            return "Empty role id.";
        } else if (StringUtils.isEmpty(roleDesc)) {
            return "Empty role desc.";
        } else if (StringUtils.isEmpty(status)) {
            return "Empty status.";
        }

        sdUserRoleService.updateUserRole(roleId, roleDesc, status);
        return null;
    }

    @Override
    public List<SdUserPathCtrlData> getParentRolePathByRoleId(String roleId) {
        List<SdUserPathCtrlData> results = new LinkedList<>();
        List<SdUserRolePathCtrlBean> userRolePath = sdPathCtrlService.getParentRolePathByRoleId(roleId);
        if (CollectionUtils.isEmpty(userRolePath)) {
            return null;
        }

        for (SdUserRolePathCtrlBean bean : userRolePath) {
            SdUserPathCtrlData data = new SdUserPathCtrlData();
            sdUserRolePathCtrlPopulator.populate(bean, data);
            results.add(data);
        }

        return results;
    }

}
