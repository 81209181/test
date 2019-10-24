package com.hkt.btu.sd.facade.impl;

import com.google.gson.Gson;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.NorarsApiFacade;
import com.hkt.btu.sd.facade.data.NorarsBsnData;
import com.hkt.btu.sd.facade.data.nora.AddressInfoBean;
import com.hkt.btu.sd.facade.data.nora.PidInfoBean;
import com.hkt.btu.sd.facade.data.nora.NoraDnGroupData;
import com.hkt.btu.sd.facade.data.nora.NorarsBsnData;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        if(StringUtils.isEmpty(bsn)){
            throw new InvalidInputException("Empty BSN.");
        }

        String apiPath = "nora/wfm/Profile.action";

        Map<String, String> queryParam = new HashMap<>(2);
        queryParam.put("bsn", bsn);
        queryParam.put("status", STATUS);

        return Optional.ofNullable(getData(apiPath, queryParam)).orElse("<h1>Test</h1>");
    }

    @Override
    public NoraDnGroupData getRelatedOfferInfoListByBsn(String bsn) {
        if(StringUtils.isEmpty(bsn)){
            throw new InvalidInputException("Empty BSN.");
        }

        String apiPath = "/norars/api/v1/ec/groupids/sr/"+bsn;
        String responseString = getData(apiPath, null);
        if(StringUtils.isEmpty(responseString)){
            return null;
        } else {
            return new Gson().fromJson(responseString, NoraDnGroupData.class);
        }
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

    @Override
    public String getServiceAddressByBsn(String bsn){
        String apiPath = "/norars/api/v1/onecomm/address/" + bsn;
        AddressInfoBean addressInfoBean = getData(apiPath, AddressInfoBean.class, null);
        if (addressInfoBean != null) {
            return addressInfoBean.getAddressString();
        }
        return null;
    }

    @Override
    public String getL1InfoByBsn(String bsn){
        String apiPath = "/norars/api/v1/onecomm/pid/" + bsn;
        PidInfoBean pidInfoBean = getData(apiPath, PidInfoBean.class, null);
        if (pidInfoBean != null) {
            return pidInfoBean.getPid() + "/" + pidInfoBean.getStb() + "/" + pidInfoBean.getDescription();
        }
        return null;
    }

    @Override
    public String getOfferDetailListByBsn(String bsn){
        String apiPath = "/norars/api/v1/onecomm/bsn/" + bsn + "/detail";
        return Optional.ofNullable(getData(apiPath, null)).orElse(null);
    }
}
