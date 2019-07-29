package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuUserGroupPathCtrlBean;
import com.hkt.btu.noc.core.dao.entity.NocUserGroupPathCtrlEntity;
import com.hkt.btu.noc.core.dao.mapper.NocUserGroupPathCtrlMapper;
import com.hkt.btu.noc.core.service.NocPathCtrlService;
import com.hkt.btu.noc.core.service.bean.NocUserGroupPathCtrlBean;
import com.hkt.btu.noc.core.service.populator.NocUserGroupPathCtrlBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

public class NocPathCtrlServiceImpl implements NocPathCtrlService {

    @Resource
    NocUserGroupPathCtrlMapper nocUserGroupPathCtrlMapper;

    @Resource(name = "pathCtrlBeanPopulator")
    NocUserGroupPathCtrlBeanPopulator pathCtrlBeanPopulator;

    public List<BtuUserGroupPathCtrlBean> getActiveCtrlBeanList() {

        List<NocUserGroupPathCtrlEntity> userGroupPathCtrlEntityList =
                nocUserGroupPathCtrlMapper.getByStatus(NocUserGroupPathCtrlEntity.STATUS_ACTIVE);

        if (CollectionUtils.isEmpty(userGroupPathCtrlEntityList)){
            return null;
        } else {
            List<BtuUserGroupPathCtrlBean> result = new LinkedList<>();

            for(NocUserGroupPathCtrlEntity pathCtrlEntity : userGroupPathCtrlEntityList){
                NocUserGroupPathCtrlBean pathCtrlBean = new NocUserGroupPathCtrlBean();
                pathCtrlBeanPopulator.populate(pathCtrlEntity, pathCtrlBean);
                result.add(pathCtrlBean);
            }

            return result;
        }
    }
}
