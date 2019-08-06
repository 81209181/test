package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.service.BtuUserGroupService;
import com.hkt.btu.common.core.service.bean.BtuUserGroupBean;
import com.hkt.btu.sd.facade.SdUserGroupFacade;
import com.hkt.btu.sd.facade.data.SdUserGroupData;
import com.hkt.btu.sd.facade.populator.SdUserGroupDataPopulator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class SdUserGroupFacadeImpl implements SdUserGroupFacade {

    @Resource(name = "userGroupService")
    BtuUserGroupService userGroupService;

    @Resource(name = "userGroupDataPopulator")
    SdUserGroupDataPopulator sdUserGroupDataPopulator;

    @Override
    public List<SdUserGroupData> listAllUserGroup() {
        List<SdUserGroupData> result = new LinkedList<>();

        List<BtuUserGroupBean> sdUserGroupBeanList = userGroupService.getAllUserGroup();
        if(CollectionUtils.isEmpty(sdUserGroupBeanList)){
            return result;
        }

        for(BtuUserGroupBean bean : sdUserGroupBeanList){
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
        List<BtuUserGroupBean> beanList = userGroupService.getEligibleUserGroupGrantList();
        if(CollectionUtils.isEmpty(beanList)){
            return null;
        }

        LinkedList<SdUserGroupData> dataList = new LinkedList<>();
        for(BtuUserGroupBean sdUserGroupBean : beanList){
            SdUserGroupData sdUserGroupData = new SdUserGroupData();
            sdUserGroupDataPopulator.populate(sdUserGroupBean, sdUserGroupData);
            dataList.add(sdUserGroupData);
        }

        dataList.sort(Comparator.comparing(SdUserGroupData::getGroupId));
        return dataList;
    }
}
