package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.service.SdPathCtrlService;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.core.service.bean.SdUserRolePathCtrlBean;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdUserOwnerAuthRoleData;
import com.hkt.btu.sd.facade.data.SdUserPathCtrlData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import com.hkt.btu.sd.facade.data.SdUserRoleWorkgroupData;
import com.hkt.btu.sd.facade.populator.SdUserOwnerAuthRoleDataPopulator;
import com.hkt.btu.sd.facade.populator.SdUserRoleDataPopulator;
import com.hkt.btu.sd.facade.populator.SdUserRolePathCtrlPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SdUserRoleFacadeImpl implements SdUserRoleFacade {

    @Resource(name = "userRoleService")
    SdUserRoleService sdUserRoleService;

    @Resource(name = "pathCtrlService")
    SdPathCtrlService sdPathCtrlService;

    @Resource(name = "userService")
    SdUserService userService;

    @Resource(name = "userRoleDataPopulator")
    SdUserRoleDataPopulator sdUserRoleDataPopulator;

    @Resource(name = "userRolePathCtrlDataPopulator")
    SdUserRolePathCtrlPopulator sdUserRolePathCtrlPopulator;

    @Resource(name = "userOwnerAuthRoleDataPopulator")
    SdUserOwnerAuthRoleDataPopulator userOwnerAuthRoleDataPopulator;

    @Override
    public List<SdUserRoleData> listAllUserRole() {
        List<SdUserRoleData> results = new LinkedList<>();
        List<SdUserRoleBean> allUserRole = sdUserRoleService.getAllUserRole();
        return getSdUserRoleData(results, allUserRole);
    }

    @Override
    public SdUserRoleData getUserRoleByRoleId(String roleId) {
        // get user role info
        SdUserRoleBean sdUserRoleBean = sdUserRoleService.getUserRoleByRoleId(roleId);
        if (sdUserRoleBean == null) {
            return null;
        }

        SdUserRoleData userRoleData = new SdUserRoleData();
        sdUserRoleDataPopulator.populate(sdUserRoleBean, userRoleData);
        return userRoleData;
    }

    @Override
    public EditResultData getUserRoleByUserId(String userId, Boolean checkTeamHead) {
        List<String> results;
        try {
            List<SdUserRoleBean> userRoleBeanList = sdUserRoleService.getUserRoleByUserId(userId, checkTeamHead);
            if (CollectionUtils.isEmpty(userRoleBeanList)) {
                return EditResultData.error("This user has no role.");
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

    @Override
    public List<SdUserRoleData> filterUserRoleList(List<SdUserRoleData> roleList) {
        return roleList.stream()
                .filter(sdUserRoleData -> !BooleanUtils.toBoolean(sdUserRoleData.getAbstractFlag()))
                .filter(sdUserRoleData -> SdUserRoleBean.ACTIVE_ROLE_STATUS.equals(sdUserRoleData.getStatus()))
                .sorted(Comparator.comparing(SdUserRoleData::getRoleDesc))
                .collect(Collectors.toList());
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
    public List<SdUserRoleData> getEligibleUserRoleList() {
        List<SdUserRoleBean> eligibleUserGroupGrantList = sdUserRoleService.getEligibleUserRoleGrantList();
        if(CollectionUtils.isEmpty(eligibleUserGroupGrantList)) {
            eligibleUserGroupGrantList.sort(Comparator.comparing(SdUserRoleBean::getRoleId));
        }
        return getSdUserRoleData(new ArrayList<>(), eligibleUserGroupGrantList);
    }



    @Override
    public String updateUserRole(String roleId, String roleDesc, String status, String abstractFlag) {
        if (StringUtils.isEmpty(roleId)) {
            return "Empty role id.";
        } else if (StringUtils.isEmpty(roleDesc)) {
            return "Empty role desc.";
        } else if (StringUtils.isEmpty(status)) {
            return "Empty status.";
        } else if (StringUtils.isEmpty(abstractFlag)) {
            return "Empty abstract flag.";
        }

        boolean isAbstract = BooleanUtils.toBoolean(abstractFlag);

        sdUserRoleService.updateUserRole(roleId, roleDesc, status, isAbstract);
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

    @Override
    public boolean checkSameTeamRole(String name, String createBy) {
        return sdUserRoleService.checkSameTeamRole(name,createBy);
    }

    @Override
    public void reloadUserRole() {
        sdUserRoleService.reloadCachedRoleTree();
    }

    @Override
    public List<SdUserRoleData> getTeamHeadRoleList() {
        List<SdUserRoleData> results = new LinkedList<>();
        List<SdUserRoleBean> allUserRole = sdUserRoleService.getTeamHeadRoleList();
        return getSdUserRoleData(results, allUserRole);
    }

    @Override
    public List<List<Object>> getRole4Chart(){
        return sdUserRoleService.getRole4Chart();
    }

    @Override
    public List<SdUserRoleData> getCurrentUserUserRole() {
        String userId = userService.getCurrentUserUserId();
        List<SdUserRoleBean> userRoleBeanList = sdUserRoleService.getUserRoleByUserId(userId, false);

        if (CollectionUtils.isEmpty(userRoleBeanList)) {
            return null;
        }

        return userRoleBeanList.stream().map(bean -> {
            SdUserRoleData data = new SdUserRoleData();
            sdUserRoleDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }


    @Override
    public List<SdUserRoleData> getAbstracParenttRole(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return null;
        }

        return sdPathCtrlService.getAbstractParentRole(roleId).stream().map(bean -> {
            SdUserRoleData data = new SdUserRoleData();
            sdUserRoleDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdUserPathCtrlData> getActivePathCtrl() {
        return sdPathCtrlService.getActivePathCtrl().stream().map(bean -> {
            SdUserPathCtrlData data = new SdUserPathCtrlData();
            sdUserRolePathCtrlPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public String createUserRolePathCtrl(String roleId, List<Integer> pathCtrlIdList) {
        if (StringUtils.isBlank(roleId)) {
            return "Please select on a Role ID.";
        }
        if (CollectionUtils.isEmpty(pathCtrlIdList)) {
            return "Please select at lease a path.";
        }
        return sdPathCtrlService.createUserRolePathCtrl(roleId, pathCtrlIdList);
    }

    @Override
    public String delUserRolePathCtrl(String roleId, int pathCtrlId) {
        if (StringUtils.isBlank(roleId)) {
            return "Empty Role Id.";
        }
        return sdPathCtrlService.delUserRolePathCtrl(roleId, pathCtrlId);
    }

    @Override
    public List<SdUserRoleData> getRole4UserOwnerAuth() {
        return sdUserRoleService.getRole4UserOwnerAuth().stream().map(bean -> {
            SdUserRoleData data = new SdUserRoleData();
            sdUserRoleDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public List<SdUserOwnerAuthRoleData> getUserOwnerAuthRoleList() {
        return sdUserRoleService.getUserOwnerAuthRoleList().stream().map(bean -> {
            SdUserOwnerAuthRoleData data = new SdUserOwnerAuthRoleData();
            userOwnerAuthRoleDataPopulator.pupulate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public String createUserOwnerAuthRole(String ownerId, String authRoleId, String serviceTypeCode) {
        if (StringUtils.isBlank(ownerId)) {
            return "Please select on a OWNER_ID.";
        }
        if (StringUtils.isBlank(authRoleId)) {
            return "Please select on a AUTH_ROLE_ID.";
        }
        if (StringUtils.equals(ownerId, authRoleId)) {
            return "OWNER_ID and AUTH_ROLE_ID must not be the same.";
        }
        if (StringUtils.isBlank(serviceTypeCode)) {
            return "Please select on a SERVICE_TYPE_CODE.";
        }
        return sdUserRoleService.createUserOwnerAuthRole(ownerId, authRoleId, serviceTypeCode);
    }

    @Override
    public String delUserOwnerAuthRole(String ownerId, String authRoleId, String serviceTypeCode) {
        if (StringUtils.isBlank(ownerId)) {
            return "Empty OWNER_ID.";
        }
        if (StringUtils.isBlank(authRoleId)) {
            return "EMPTY AUTH_ROLE_ID.";
        }
        if (StringUtils.isBlank(serviceTypeCode)) {
            return "EMPTY SERVICE_TYPE_CODE.";
        }
        return sdUserRoleService.delUserOwnerAuthRole(ownerId, authRoleId, serviceTypeCode);
    }

    @Override
    public List<SdUserRoleWorkgroupData> getUserRoleWorkgroupList() {
        return sdUserRoleService.getUserRoleWorkgroupList().stream().map(bean -> {
            SdUserRoleWorkgroupData data = new SdUserRoleWorkgroupData();
            BeanUtils.copyProperties(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public String delUserRoleWorkgroup(String roleId, String workgroup) {
        if (StringUtils.isBlank(roleId)) {
            return "Empty ROLE_ID";
        }
        if (StringUtils.isBlank(workgroup)) {
            return "Empty WORKGROUP";
        }
        return sdUserRoleService.delUserRoleWorkgroup(roleId, workgroup);
    }

    @Override
    public String createUserRoleWorkgroup(String roleId, String workgroup) {
        if (StringUtils.isBlank(roleId)) {
            return "Please select on a role id.";
        }
        if (StringUtils.isBlank(workgroup)) {
            return "Please select on a workgroup";
        }
        return sdUserRoleService.createUserRoleWorkgroup(roleId, workgroup);
    }

    @Override
    public List<String> getWorkgroupList() {
        return sdUserRoleService.getWorkgroupList();
    }
}
