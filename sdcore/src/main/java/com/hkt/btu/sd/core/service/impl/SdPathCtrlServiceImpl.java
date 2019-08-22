package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuUserRolePathCtrlBean;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupPathCtrlEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRolePathCtrlEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserRolePathCtrlMapper;
import com.hkt.btu.sd.core.service.SdPathCtrlService;
import com.hkt.btu.sd.core.service.bean.SdUserRolePathCtrlBean;
import com.hkt.btu.sd.core.service.populator.SdUserRolePathCtrlBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

public class SdPathCtrlServiceImpl implements SdPathCtrlService {

    @Resource
    SdUserRolePathCtrlMapper sdUserRolePathCtrlMapper;

    @Resource(name = "pathCtrlBeanPopulator")
    SdUserRolePathCtrlBeanPopulator pathCtrlBeanPopulator;

    public List<BtuUserRolePathCtrlBean> getActiveCtrlBeanList() {

        List<SdUserRolePathCtrlEntity> userRolePathCtrlEntityList =
                sdUserRolePathCtrlMapper.getByStatus(SdUserGroupPathCtrlEntity.STATUS_ACTIVE);

        if (CollectionUtils.isEmpty(userRolePathCtrlEntityList)){
            return null;
        } else {
            List<BtuUserRolePathCtrlBean> result = new LinkedList<>();

            for(SdUserRolePathCtrlEntity pathCtrlEntity : userRolePathCtrlEntityList){
                SdUserRolePathCtrlBean pathCtrlBean = new SdUserRolePathCtrlBean();
                pathCtrlBeanPopulator.populate(pathCtrlEntity, pathCtrlBean);
                result.add(pathCtrlBean);
            }

            return result;
        }
    }
}
