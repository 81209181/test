package com.hkt.btu.common.facade.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuApiClientService;
import com.hkt.btu.common.core.service.BtuAuditTrailService;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.facade.BtuApiClientFacade;
import com.hkt.btu.common.facade.data.BtuApiUserData;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;

public class BtuApiClientFacadeImpl implements BtuApiClientFacade {


    @Resource(name = "userService")
    BtuUserService userService;
    @Resource(name = "configParamService")
    BtuConfigParamService configParamService;
    @Resource(name = "apiClientService")
    BtuApiClientService apiClientService;
    @Resource(name = "auditTrailService")
    BtuAuditTrailService auditTrailService;


    @Override
    public String getApiAuthKey(String apiName){
        auditTrailService.insertViewApiAuthAuditTrail(apiName);
        return apiClientService.getApiClientKey(apiName);
    }

    @Override
    public void regenerateApiClientKey(String apiName) throws GeneralSecurityException {
        // add audit trail
        auditTrailService.insertRegenApiAuthAuditTrail(apiName);

        // re-generate a new key and update config param
        apiClientService.regenerateApiClientKey(apiName);
    }



    @Override
    public void reloadCache(String apiName) {
        apiClientService.reloadApiClientKey(apiName);
    }

    @Override
    public List<BtuApiUserData> getApiUser() {
        List<BtuUserBean> beanList = userService.getApiUser();
        if(CollectionUtils.isEmpty(beanList)){
            return new LinkedList<>();
        }

        List<BtuApiUserData> dataList = new LinkedList<>();
        for (BtuUserBean userBean : beanList) {
            BtuApiUserData data = new BtuApiUserData();

            String apiClient = String.format("%s.key", userBean.getName());
            BtuConfigParamBean configParamBean = new BtuConfigParamBean();
            BtuConfigParamBean btuConfigParamBean = configParamService.getConfigParamByGroupAndKey(BtuConfigParamEntity.API_CLIENT.CONFIG_GROUP, apiClient);
            if (btuConfigParamBean!=null) {
                configParamBean = btuConfigParamBean;
            }

            String configValue = configParamBean.getConfigValue();
            boolean validCacheSync = apiClientService.checkApiClientKey(userBean.getName(), configValue);
            if (validCacheSync) {
                data.setCacheSync(BtuApiUserData.CACHE_SYNC.YES);
            } else {
                data.setCacheSync(BtuApiUserData.CACHE_SYNC.NO);
            }

            data.setName(userBean.getName());
            dataList.add(data);
        }
        return dataList;
    }
}
