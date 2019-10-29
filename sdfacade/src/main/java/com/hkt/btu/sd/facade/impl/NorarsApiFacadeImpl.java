package com.hkt.btu.sd.facade.impl;

import com.google.gson.Gson;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.NorarsApiFacade;
import com.hkt.btu.sd.facade.data.ServiceAddressData;
import com.hkt.btu.sd.facade.data.nora.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NorarsApiFacadeImpl extends AbstractRestfulApiFacade implements NorarsApiFacade {

    private final static String STATUS = "W";

    @Resource(name = "apiService")
    SdApiService apiService;

    @Resource(name = "userService")
    SdUserService userService;

    @Override
    public String getBsnByDn(String dn) {
        String apiPath = "/norars/api/v1/onecomm/bsn/" + dn;

        return getData(apiPath, null);
    }

    @Override
    public NoraBroadbandInfoData getOfferDetailListByBsn(String bsn) {
        if(StringUtils.isEmpty(bsn)){
            throw new InvalidInputException("Empty BSN.");
        }

        // find parent bsn for dn input
        if(StringUtils.length(bsn)==8){
            bsn = getBsnByDn(bsn);
        }

        String apiPath = "/norars/api/v1/onecomm/bsn/" + bsn + "/detail";
        NoraBroadbandInfoData data = getData(apiPath, NoraBroadbandInfoData.class, null);

        return data;
    }

    @Override
    public String getInventory(String bsn) {
        if(StringUtils.isEmpty(bsn)){
            throw new InvalidInputException("Empty BSN.");
        }

        // find parent bsn for dn input
        if(StringUtils.length(bsn)==8){
            bsn = getBsnByDn(bsn);
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

        // find parent bsn for dn input
        if(StringUtils.length(bsn)==8){
            bsn = getBsnByDn(bsn);
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
    public boolean resetNGN3PWD(String bsn) {
        Optional.ofNullable(getRelatedOfferInfoListByBsn(bsn)).map(NoraDnGroupData::getAdminPortalId).ifPresentOrElse(admin -> {
            if (admin.contains(",")) {
                Arrays.stream(admin.split(","));
            } else {

            }
        },() -> {
            throw new RuntimeException("Admin portal Id not found.");
        });
        return false;
    }

    @Override
    public NoraAccountData getNGN3OneDayAdminAccount(String bsn) {
        String requestorId = userService.getCurrentUserBean().getUserId();

        if(StringUtils.length(bsn) == 8) {
            bsn = getBsnByDn(bsn);
        }

        String apiPath = "/norars/api/v1/osb/accounts/"+bsn+"/"+ requestorId;
        NoraAccountData data = postData(apiPath, NoraAccountData.class, null, null);

        return data;
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
    public ServiceAddressData getServiceAddressByBsn(String bsn){
        // find parent bsn for dn input
        if(StringUtils.length(bsn)==8){
            bsn = getBsnByDn(bsn);
        }

        String apiPath = "/norars/api/v1/onecomm/address/" + bsn;
        NoraAddressInfoData noraAddressInfoData = getData(apiPath, NoraAddressInfoData.class, null);
        if (noraAddressInfoData != null) {
            ServiceAddressData data = new ServiceAddressData();
            data.setServiceAddress(getAddressString(noraAddressInfoData));
            data.setGridId(noraAddressInfoData.getAddr14());
            data.setExchangeBuildingId(noraAddressInfoData.getAddr15());
            return data;
        }
        return null;
    }

    @Override
    public String getL1InfoByBsn(String bsn){
        // find parent bsn for dn input
        if(StringUtils.length(bsn)==8){
            bsn = getBsnByDn(bsn);
        }

        String apiPath = "/norars/api/v1/onecomm/pid/" + bsn;
        NoraPidInfoData noraPidInfoData = getData(apiPath, NoraPidInfoData.class, null);
        if (noraPidInfoData != null) {
            return noraPidInfoData.getPid() + "/" + noraPidInfoData.getStb() + "/" + noraPidInfoData.getDescription();
        }
        return null;
    }

    private String getAddressString(NoraAddressInfoData noraAddressInfoData) {
        String result = appendStringWithDelimiter("", noraAddressInfoData.getAddr1(), ", ", "FLAT %s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr2(), ", ", "LOT %s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr3(), ", ", "%s/F");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr4(), ", ", "BLOCK %s");

        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr5(), ", ", "%s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr6(), ", ", "%s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr7(), ", ", "%s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr8(), ", ", "%s");

        // Street number and name
        String street = appendStringWithDelimiter("", noraAddressInfoData.getAddr9(), "", "%s");
        street = appendStringWithDelimiter(street, noraAddressInfoData.getAddr10(), " ", "- %s");
        street = appendStringWithDelimiter(street, noraAddressInfoData.getAddr11(), " ", "%s");
        result = appendStringWithDelimiter(result, street, ", ", "%s");

        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr12(), ", ", "%s");
        result = appendStringWithDelimiter(result, noraAddressInfoData.getAddr13(), ", ", "%s");

        return result;
    }

    private String appendStringWithDelimiter(String result, String addr, String str1, String str2){
        if (StringUtils.isEmpty(result)) {
            if (StringUtils.isNotEmpty(addr)) {
                result = String.format(result+str2,addr);
            }
        } else {
            if (StringUtils.isNotEmpty(addr)) {
                result = String.format(result+str1+str2,addr);
            }
        }
        return result;
    }
}
