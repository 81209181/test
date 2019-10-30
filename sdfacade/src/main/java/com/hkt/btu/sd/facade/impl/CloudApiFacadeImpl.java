package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.CloudApiFacade;

import javax.annotation.Resource;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class CloudApiFacadeImpl extends AbstractRestfulApiFacade implements CloudApiFacade {

    @Resource(name = "apiService")
    SdApiService apiService;

    @Override
    protected SiteInterfaceBean getTargetApiSiteInterfaceBean() {
        return null;
    }

    @Override
    protected Invocation.Builder getInvocationBuilder(WebTarget webTarget) {
        return webTarget.request(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,getBtuHeaderAuthKey(getTargetApiSiteInterfaceBean()));
    }

    @Override
    public String resetNgn3Pwd(String account) {
        String apiPath = String.format("/api/v1/osb/accounts/%s/nil",account);
        return "not ready";
    }
}
