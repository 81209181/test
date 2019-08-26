package com.hkt.btu.sd.core.service.populator;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;

import java.util.List;

public class SdSiteInterfaceBeanPopulator {

    public void populate(List<SdConfigParamEntity> entities, SiteInterfaceBean bean) {
        for (SdConfigParamEntity entity : entities) {
            if (entity.getConfigKey().equalsIgnoreCase("systemName")) {
                bean.setSystemName(entity.getConfigValue());
            }else if (entity.getConfigKey().equalsIgnoreCase("url")){
                bean.setUrl(entity.getConfigValue());
            } else if (entity.getConfigKey().equalsIgnoreCase("userName")) {
                bean.setUserName(entity.getConfigValue());
            } else if (entity.getConfigKey().equalsIgnoreCase("password")) {
                bean.setPassword(entity.getConfigValue());
            } else if (entity.getConfigKey().equalsIgnoreCase("xAppKey")) {
                bean.setxAppkey(entity.getConfigValue());
            } else if (entity.getConfigKey().equalsIgnoreCase("beId")) {
                bean.setBeId(entity.getConfigValue());
            } else if (entity.getConfigKey().equalsIgnoreCase("channelType")) {
                bean.setChannelType(entity.getConfigValue());
            }
        }
    }
}
