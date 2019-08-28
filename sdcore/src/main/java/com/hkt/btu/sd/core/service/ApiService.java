package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.util.List;

public class ApiService {

    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;

    protected SiteInterfaceBean getSiteInterfaceBean(List<SdConfigParamEntity> entities){
        SiteInterfaceBean bean =new SiteInterfaceBean();
        for (SdConfigParamEntity entity : entities) {
            String value = entity.getConfigValue();
            if (entity.getEncrypt().equalsIgnoreCase(BtuConfigParamEntity.ENCRYPT.YES)) {
                value = sensitiveDataService.decryptToStringSafe(Base64Utils.decodeFromString(entity.getConfigValue()));
            }
            if (entity.getConfigKey().equalsIgnoreCase("systemName")) {
                bean.setSystemName(value);
            }else if (entity.getConfigKey().equalsIgnoreCase("url")){
                bean.setUrl(value);
            } else if (entity.getConfigKey().equalsIgnoreCase("userName")) {
                bean.setUserName(value);
            } else if (entity.getConfigKey().equalsIgnoreCase("password")) {
                bean.setPassword(value);
            } else if (entity.getConfigKey().equalsIgnoreCase("xAppKey")) {
                bean.setxAppkey(value);
            } else if (entity.getConfigKey().equalsIgnoreCase("beId")) {
                bean.setBeId(value);
            } else if (entity.getConfigKey().equalsIgnoreCase("channelType")) {
                bean.setChannelType(value);
            }
        }
        return bean;
    }
}
