package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.sd.core.service.SdApiClientService;
import com.hkt.btu.sd.core.service.SdAuditTrailService;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import com.hkt.btu.sd.facade.SdApiClientFacade;
import com.hkt.btu.sd.facade.data.SdApiUserData;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SdApiClientFacadeImpl implements SdApiClientFacade {

    @Resource(name = "apiClientService")
    SdApiClientService sdApiClientService;
    @Resource(name = "auditTrailService")
    SdAuditTrailService sdAuditTrailService;
    @Resource(name = "userService")
    SdUserService sdUserService;
    @Resource(name = "configParamService")
    SdConfigParamService sdConfigParamService;

    @Override
    public List<SdApiUserData> getApiUser() {
        List<SdUserBean> beanList = sdUserService.getApiUser();
        List<SdApiUserData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdUserBean userBean : beanList) {
                SdApiUserData data = new SdApiUserData();

                String apiClient = String.format("%s.key", userBean.getName());
                SdConfigParamBean configParamBean = new SdConfigParamBean();
                Optional<SdConfigParamBean> sdConfigParamBean = sdConfigParamService.getConfigParamByGroupAndKey(BtuConfigParamEntity.API_CLIENT.CONFIG_GROUP, apiClient);
                if (!sdConfigParamBean.isEmpty()) {
                    configParamBean = sdConfigParamBean.get();
                }

                String configValue = configParamBean.getConfigValue();
                boolean validCacheSync = sdApiClientService.checkApiClientKey(userBean.getName(), configValue);
                if (validCacheSync) {
                    data.setCacheSync(SdApiUserData.CACHE_SYNC.YES);
                } else {
                    data.setCacheSync(SdApiUserData.CACHE_SYNC.NO);
                }

                data.setName(userBean.getName());
                dataList.add(data);
            }
        }
        return dataList;
    }

    @Override
    public String getApiClientBean(String apiName){
        sdAuditTrailService.insertViewApiAuthAuditTrail(apiName);
        return sdApiClientService.getApiClientKey(apiName);
    }

    @Override
    public void regenerateApiClientKey(String apiName) throws GeneralSecurityException {
        // add audit trail
        sdAuditTrailService.insertRegenApiAuthAuditTrail(apiName);

        // re-generate a new key and update config param
        sdApiClientService.regenerateApiClientKey(apiName);
    }

    @Override
    public void reloadCache(String apiName) {
        sdApiClientService.reloadApiClientKey(apiName);
    }
}
