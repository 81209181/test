package com.hkt.btu.common.facade.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuApiClientService;
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
import java.util.Optional;

public class BtuApiClientFacadeImpl implements BtuApiClientFacade {


    @Resource(name = "userService")
    BtuUserService btuUserService;
    @Resource(name = "configParamService")
    BtuConfigParamService btuConfigParamService;
    @Resource(name = "apiClientService")
    BtuApiClientService btuApiClientService;


    @Override
    public String getApiAuthKey(String apiName){
        return btuApiClientService.getApiClientKey(apiName);
    }

    @Override
    public void regenerateApiClientKey(String apiName) throws GeneralSecurityException {
        btuApiClientService.regenerateApiClientKey(apiName);
    }



    @Override
    public void reloadCache(String apiName) {
        btuApiClientService.reloadApiClientKey(apiName);
    }

    @Override
    public List<BtuApiUserData> getApiUser() {
        List<BtuUserBean> beanList = btuUserService.getApiUser();
        if(CollectionUtils.isEmpty(beanList)){
            return new LinkedList<>();
        }

        List<BtuApiUserData> dataList = new LinkedList<>();
        for (BtuUserBean userBean : beanList) {
            BtuApiUserData data = new BtuApiUserData();

            String apiClient = String.format("%s.key", userBean.getName());
            BtuConfigParamBean configParamBean = new BtuConfigParamBean();
            Optional<BtuConfigParamBean> btuConfigParamBean = btuConfigParamService.getConfigParamByGroupAndKey(BtuConfigParamEntity.API_CLIENT.CONFIG_GROUP, apiClient);
            if (btuConfigParamBean.isPresent()) {
                configParamBean = btuConfigParamBean.get();
            }

            String configValue = configParamBean.getConfigValue();
            boolean validCacheSync = btuApiClientService.checkApiClientKey(userBean.getName(), configValue);
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
