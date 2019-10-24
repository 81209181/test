package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.NorarsApiFacade;
import com.hkt.btu.sd.facade.data.NorarsBsnData;

import javax.annotation.Resource;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

public class NorarsApiFacadeImpl extends AbstractRestfulApiFacade implements NorarsApiFacade {

    private final static String STATUS = "W";

    @Resource(name = "apiService")
    SdApiService apiService;

    @Override
    public NorarsBsnData getBsnByDn(String dn) {
        String apiPath = "/norars/api/v1/onecomm/dn/" + dn;

        return getData(apiPath, NorarsBsnData.class, null);
    }

    @Override
    public String getInventory(String bsn) {
        String apiPath = "nora/wfm/Profile.action";

        Map<String, String> queryParam = new HashMap<>(2);
        queryParam.put("bsn", bsn);
        queryParam.put("status", STATUS);

        return Optional.ofNullable(getData(apiPath, queryParam)).orElse("<h1>Test</h1>");
    }

    @Override
    protected SiteInterfaceBean getTargetApiSiteInterfaceBean() {
        return apiService.getSiteInterfaceBean(SiteInterfaceBean.API_NORARS.API_NAME);
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
