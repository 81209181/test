package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.ApiService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.ItsmApiFacade;
import com.hkt.btu.sd.facade.data.ItsmProfileData;
import com.hkt.btu.sd.facade.data.ItsmSearchProfileResponseData;
import com.hkt.btu.sd.facade.data.ItsmTenantData;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class ItsmApiFacadeImpl extends AbstractRestfulApiFacade implements ItsmApiFacade {

    @Resource(name = "apiService")
    ApiService apiService;

    @Override
    protected SiteInterfaceBean getTargetApiSiteInterfaceBean() {
        return apiService.getSiteInterfaceBean(SiteInterfaceBean.API_ITSM_RESTFUL.API_NAME);
    }

    @Override
    protected Invocation.Builder getInvocationBuilder(WebTarget webTarget) {
        SiteInterfaceBean siteInterfaceBean = getTargetApiSiteInterfaceBean();
        String headerAuth = getBtuHeaderAuthKey(siteInterfaceBean);
        return webTarget.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, headerAuth);
    }

    @Override
    public ItsmSearchProfileResponseData searchProfileByCustName(String custName) {
        return searchProfile("custName", custName);
    }

    @Override
    public ItsmSearchProfileResponseData searchProfileByCustId(String custId) {
        return searchProfile("custId", custId);
    }

    @Override
    public ItsmSearchProfileResponseData searchProfileByServiceNo(String serviceNo) {
        return searchProfile("serviceNo", serviceNo);
    }

    @Override
    public ItsmSearchProfileResponseData searchProfileByTenantId(String tenantId) {
        return searchProfile("tenantId", tenantId);
    }

    @Override
    public ItsmSearchProfileResponseData searchProfileByDomainName(String domainName) {
        return searchProfile("domainName", domainName);
    }

    @Override
    public ItsmTenantData getTenant(int tenantId) {
        String apiPath = "/cloudrs/api/v1/sa/tenant/" + tenantId;
        return postData(apiPath, ItsmTenantData.class, null, null);
    }

    private ItsmSearchProfileResponseData searchProfile(String searchKey, String searchValue) {

        String apiPath = "/cloudrs/api/v1/sa/search/" + searchKey + "/" + searchValue;
        ItsmSearchProfileResponseData itsmSearchProfileResponseData =
                postData(apiPath, ItsmSearchProfileResponseData.class, null, null);

        List<ItsmProfileData> itsmProfileDataList = itsmSearchProfileResponseData==null ? null : itsmSearchProfileResponseData.getList();
        if(CollectionUtils.isEmpty(itsmProfileDataList)){
            return itsmSearchProfileResponseData;
        }

        // fill-in itsm linking url
        SiteInterfaceBean itsmSiteBean = apiService.getSiteInterfaceBean(SiteInterfaceBean.API_ITSM.API_NAME);
        String itsmUrl = itsmSiteBean.getUrl();
        String resourcePoolApiPath = "/info/ResourcePoolTab.action?resourceId=";
        for( ItsmProfileData itsmProfileData : itsmProfileDataList ){
            String linkingUrl = String.format("%s%s%s", itsmUrl, resourcePoolApiPath, itsmProfileData.getResourceId());
            itsmProfileData.setUrl(linkingUrl);
        }

        return itsmSearchProfileResponseData;

    }

}
