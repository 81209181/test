package com.hkt.btu.sd.core.service.impl;


import com.hkt.btu.common.core.service.bean.BtuKeyStoreConfigBean;
import com.hkt.btu.common.core.service.impl.BtuSensitiveDataServiceImpl;
import com.hkt.btu.sd.core.service.SdSensitiveDataService;
import org.springframework.beans.factory.annotation.Value;

public class SdSensitiveDataServiceImpl extends BtuSensitiveDataServiceImpl implements SdSensitiveDataService {

    // keystore
    @Value("${servicedesk.aesKeystore.path}")
    private String KEYSTORE_PATH;
    @Value("${servicedesk.aesKeystore.storePass}")
    private String STORE_PASS;
    private final char[] KEY_PASS = "servicedesk".toCharArray();

    protected BtuKeyStoreConfigBean getBtuKeyStoreBeanInternal(){
        BtuKeyStoreConfigBean keyStoreConfigBean = new BtuKeyStoreConfigBean();
        keyStoreConfigBean.setKeystorePath(KEYSTORE_PATH);
        keyStoreConfigBean.setStorePass(STORE_PASS);
        keyStoreConfigBean.setKeyPass(KEY_PASS);
        return keyStoreConfigBean;
    }
}
