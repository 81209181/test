package com.hkt.btu.noc.facade.impl;

import com.hkt.btu.noc.core.service.NocUserGroupService;
import com.hkt.btu.noc.core.service.bean.NocUserGroupBean;
import com.hkt.btu.noc.facade.NocUserGroupFacade;
import com.hkt.btu.noc.facade.data.NocUserGroupData;
import com.hkt.btu.noc.facade.populator.NocUserGroupDataPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class NocUserGroupFacadeImpl implements NocUserGroupFacade {

//    @Resource(name = "userGroupService")
    @Autowired
    NocUserGroupService nocUserGroupService;

    @Autowired
    NocUserGroupDataPopulator nocUserGroupDataPopulator;

    @Override
    public List<NocUserGroupData> listAllUserGroup() {
        List<NocUserGroupData> result = new LinkedList<>();

        List<NocUserGroupBean> nocUserGroupBeanList = nocUserGroupService.getAllUserGroup();
        if(CollectionUtils.isEmpty(nocUserGroupBeanList)){
            return result;
        }

        for(NocUserGroupBean bean : nocUserGroupBeanList){
            NocUserGroupData nocUserGroupData = new NocUserGroupData();
            nocUserGroupDataPopulator.populate(bean, nocUserGroupData);
            result.add(nocUserGroupData);
        }

        return result;
    }

    @Override
    public HashMap<String, NocUserGroupData> getUserGroupMap(List<NocUserGroupData> userGroupDataList){
        HashMap<String, NocUserGroupData> result = new HashMap<>();

        if(CollectionUtils.isEmpty(userGroupDataList)){
            return result;
        }

        for(NocUserGroupData userGroupData : userGroupDataList){
            result.put(userGroupData.getGroupId(), userGroupData);
        }

        return result;
    }

    @Override
    public LinkedList<NocUserGroupData> getEligibleUserGroupList() {
        List<NocUserGroupBean> beanList = nocUserGroupService.getEligibleUserGroupGrantList();
        if(CollectionUtils.isEmpty(beanList)){
            return null;
        }

        LinkedList<NocUserGroupData> dataList = new LinkedList<>();
        for(NocUserGroupBean nocUserGroupBean : beanList){
            NocUserGroupData nocUserGroupData = new NocUserGroupData();
            nocUserGroupDataPopulator.populate(nocUserGroupBean, nocUserGroupData);
            dataList.add(nocUserGroupData);
        }

        dataList.sort(Comparator.comparing(NocUserGroupData::getGroupId));
        return dataList;
    }
}
