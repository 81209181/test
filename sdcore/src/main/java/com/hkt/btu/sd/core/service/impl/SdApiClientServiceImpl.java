package com.hkt.btu.sd.core.service.impl;


import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.impl.BtuApiClientServiceImpl;
import com.hkt.btu.sd.core.service.SdApiClientService;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.util.UUID;

public class SdApiClientServiceImpl extends BtuApiClientServiceImpl implements SdApiClientService {
    private static final Logger LOG = LogManager.getLogger(SdApiClientServiceImpl.class);

    @Resource(name = "configParamService")
    SdConfigParamService sdConfigParamService;

    @Override
    public void regenerateApiClientKey(String apiName) {
        UUID uuid = UUID.randomUUID();
        String configKey = String.format("%s.key", apiName);
        String configValue = uuid.toString();

        try{
            sdConfigParamService.updateConfigParam(BtuConfigParamEntity.API_CLIENT.CONFIG_GROUP, configKey, configValue,
                    BtuConfigParamEntity.TYPE.STRING, BtuConfigParamEntity.ENCRYPT.YES);
            LOG.info("Re-generated API Client key: {}", apiName);
        } catch (GeneralSecurityException e) {
            LOG.error(e.getMessage());
        }
    }
}
