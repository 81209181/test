package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;
import com.hkt.btu.common.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.facade.ItsmApiFacade;
import com.hkt.btu.sd.facade.data.itsm.ItsmProfileData;
import com.hkt.btu.sd.facade.data.itsm.ItsmSearchProfileResponseData;
import com.hkt.btu.sd.facade.data.itsm.ItsmTenantData;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

public class ItsmApiFacadeImpl extends AbstractRestfulApiFacade implements ItsmApiFacade {

    @Resource(name = "apiService")
    SdApiService apiService;

    @Override
    protected BtuApiProfileBean getTargetApiProfile() {
        return apiService.getBesApiProfileBean();
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
        String apiPath = "/api/v1/sa/tenant/" + tenantId;
        return postData(apiPath, ItsmTenantData.class, null, null);
    }

    private ItsmSearchProfileResponseData searchProfile(String searchKey, String searchValue) {

        String apiPath = "/api/v1/sa/search/" + searchKey + "/" + searchValue;
        ItsmSearchProfileResponseData itsmSearchProfileResponseData =
                postData(apiPath, ItsmSearchProfileResponseData.class, null, null);

        List<ItsmProfileData> itsmProfileDataList = itsmSearchProfileResponseData==null ? null : itsmSearchProfileResponseData.getList();
        if(CollectionUtils.isEmpty(itsmProfileDataList)){
            return itsmSearchProfileResponseData;
        }

        // fill-in itsm linking url
        BtuApiProfileBean itsmSiteBean = apiService.getItsmApiProfileBean();
        String itsmUrl = itsmSiteBean.getUrl();
        String resourcePoolApiPath = "/info/ResourcePoolTab.action?resourceId=";
        for( ItsmProfileData itsmProfileData : itsmProfileDataList ){
            String linkingUrl = String.format("%s%s%s", itsmUrl, resourcePoolApiPath, itsmProfileData.getResourceId());
            itsmProfileData.setUrl(linkingUrl);
        }

        return itsmSearchProfileResponseData;

    }

}
