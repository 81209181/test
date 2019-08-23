package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.NorarsApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.NorarsApiFacade;
import com.hkt.btu.sd.facade.data.NorarsBsnData;

import javax.annotation.Resource;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class NorarsApiFacadeImpl extends AbstractRestfulApiFacade implements NorarsApiFacade {

    @Resource(name = "norarsApiService")
    NorarsApiService norarsApiService;

    @Override
    public NorarsBsnData getBsnByDn(String dn) {
        String apiPath = "/norars/api/v1/onecomm/dn/" + dn;

        return getData(apiPath, NorarsBsnData.class, null);
    }

    @Override
    protected SiteInterfaceBean getTargetApiSiteInterfaceBean() {
        return norarsApiService.getNorarsRestfulApiBean();
    }

    @Override
    protected Invocation.Builder getInvocationBuilder(WebTarget webTarget) {
        SiteInterfaceBean siteInterfaceBean = getTargetApiSiteInterfaceBean();
        String headerAuth = getBtuHeaderAuthKey(siteInterfaceBean);
        return webTarget
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, headerAuth);
    }
}
