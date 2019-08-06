package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuUserGroupPathCtrlBean;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupPathCtrlEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserGroupPathCtrlMapper;
import com.hkt.btu.sd.core.service.SdPathCtrlService;
import com.hkt.btu.sd.core.service.bean.SdUserGroupPathCtrlBean;
import com.hkt.btu.sd.core.service.populator.SdUserGroupPathCtrlBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

public class SdPathCtrlServiceImpl implements SdPathCtrlService {

    @Resource
    SdUserGroupPathCtrlMapper sdUserGroupPathCtrlMapper;

    @Resource(name = "pathCtrlBeanPopulator")
    SdUserGroupPathCtrlBeanPopulator pathCtrlBeanPopulator;

    public List<BtuUserGroupPathCtrlBean> getActiveCtrlBeanList() {

        List<SdUserGroupPathCtrlEntity> userGroupPathCtrlEntityList =
                sdUserGroupPathCtrlMapper.getByStatus(SdUserGroupPathCtrlEntity.STATUS_ACTIVE);

        if (CollectionUtils.isEmpty(userGroupPathCtrlEntityList)){
            return null;
        } else {
            List<BtuUserGroupPathCtrlBean> result = new LinkedList<>();

            for(SdUserGroupPathCtrlEntity pathCtrlEntity : userGroupPathCtrlEntityList){
                SdUserGroupPathCtrlBean pathCtrlBean = new SdUserGroupPathCtrlBean();
                pathCtrlBeanPopulator.populate(pathCtrlEntity, pathCtrlBean);
                result.add(pathCtrlBean);
            }

            return result;
        }
    }
}
