package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdUserGroupService;
import com.hkt.btu.sd.core.service.bean.SdUserGroupBean;
import com.hkt.btu.sd.facade.SdUserGroupFacade;
import com.hkt.btu.sd.facade.data.SdUserGroupData;
import com.hkt.btu.sd.facade.populator.SdUserGroupDataPopulator;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SdUserGroupFacadeImpl implements SdUserGroupFacade {

    @Resource(name = "userGroupService")
    SdUserGroupService sdUserGroupService;

    @Resource(name = "userGroupDataPopulator")
    SdUserGroupDataPopulator sdUserGroupDataPopulator;

    @Override
    public List<SdUserGroupData> listAllUserGroup() {
        List<SdUserGroupData> result = new LinkedList<>();

        List<SdUserGroupBean> sdUserGroupBeanList = sdUserGroupService.getAllUserGroup();
        if(CollectionUtils.isEmpty(sdUserGroupBeanList)){
            return result;
        }

        for(SdUserGroupBean bean : sdUserGroupBeanList){
            SdUserGroupData sdUserGroupData = new SdUserGroupData();
            sdUserGroupDataPopulator.populate(bean, sdUserGroupData);
            result.add(sdUserGroupData);
        }

        return result;
    }

    @Override
    public HashMap<String, SdUserGroupData> getUserGroupMap(List<SdUserGroupData> userGroupDataList){
        HashMap<String, SdUserGroupData> result = new HashMap<>();

        if(CollectionUtils.isEmpty(userGroupDataList)){
            return result;
        }

        for(SdUserGroupData userGroupData : userGroupDataList){
            result.put(userGroupData.getGroupId(), userGroupData);
        }

        return result;
    }

    @Override
    public LinkedList<SdUserGroupData> getEligibleUserGroupList() {
        List<SdUserGroupBean> beanList = sdUserGroupService.getEligibleUserGroupGrantList();
        if(CollectionUtils.isEmpty(beanList)){
            return null;
        }

        LinkedList<SdUserGroupData> dataList = new LinkedList<>();
        for(SdUserGroupBean sdUserGroupBean : beanList){
            SdUserGroupData sdUserGroupData = new SdUserGroupData();
            sdUserGroupDataPopulator.populate(sdUserGroupBean, sdUserGroupData);
            dataList.add(sdUserGroupData);
        }

        dataList.sort(Comparator.comparing(SdUserGroupData::getGroupId));
        return dataList;
    }
}
