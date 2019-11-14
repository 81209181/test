package com.hkt.btu.sd.facade.data.wfm;

import com.hkt.btu.common.facade.data.DataInterface;

public class WfmResponseTokenData implements DataInterface {

    private String jwt;
    private String url;

    private WfmResponseTokenData(String jwt, String url) {
        this.jwt = jwt;
        this.url = url;
    }

    public static WfmResponseTokenData of(String jwt, String url) {
        return new WfmResponseTokenData(jwt, url);
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
