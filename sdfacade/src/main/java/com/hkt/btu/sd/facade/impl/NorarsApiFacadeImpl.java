package com.hkt.btu.sd.facade.impl;

import com.google.gson.Gson;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.NorarsApiFacade;
import com.hkt.btu.sd.facade.data.nora.NoraBroadbandInfoData;
import com.hkt.btu.sd.facade.data.nora.NoraAddressInfoData;
import com.hkt.btu.sd.facade.data.nora.NoraPidInfoData;
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
    public NoraBroadbandInfoData getOfferDetailListByBsn(String bsn) {
        if(StringUtils.isEmpty(bsn)){
            throw new InvalidInputException("Empty BSN.");
        }

        String apiPath = "/norars/api/v1/onecomm/bsn/" + bsn + "/detail";
        String responseString = getData(apiPath, null);
        if(StringUtils.isEmpty(responseString)){
            return null;
        } else {
            return new Gson().fromJson(responseString, NoraBroadbandInfoData.class);
        }
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
        NoraAddressInfoData noraAddressInfoData = getData(apiPath, NoraAddressInfoData.class, null);
        if (noraAddressInfoData != null) {
            return noraAddressInfoData.getAddressString();
        }
        return null;

        // todo [RICO]: return ServiceAddressData
    }

    @Override
    public String getL1InfoByBsn(String bsn){
        String apiPath = "/norars/api/v1/onecomm/pid/" + bsn;
        NoraPidInfoData noraPidInfoData = getData(apiPath, NoraPidInfoData.class, null);
        if (noraPidInfoData != null) {
            return noraPidInfoData.getPid() + "/" + noraPidInfoData.getStb() + "/" + noraPidInfoData.getDescription();
        }
        return null;
    }
}
