package com.hkt.btu.common.core.service.bean;


public class BtuKeyStoreConfigBean extends BaseBean {

    private String keystorePath;
    private String storePass;
    private char[] keyPass;

    public String getKeystorePath() {
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    public String getStorePass() {
        return storePass;
    }

    public void setStorePass(String storePass) {
        this.storePass = storePass;
    }

    public char[] getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(char[] keyPass) {
        this.keyPass = keyPass;
    }
}
