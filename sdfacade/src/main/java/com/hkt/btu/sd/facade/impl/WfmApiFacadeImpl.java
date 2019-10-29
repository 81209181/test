package com.hkt.btu.sd.facade.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkt.btu.common.facade.data.DataInterface;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.WfmApiFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.data.wfm.WfmJobData;
import com.hkt.btu.sd.facade.data.wfm.WfmOfferNameProductTypeData;
import com.hkt.btu.sd.facade.data.wfm.WfmPendingOrderData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class WfmApiFacadeImpl extends AbstractRestfulApiFacade implements WfmApiFacade {
    private static final Logger LOG = LogManager.getLogger(WfmApiFacadeImpl.class);

    @Resource(name = "apiService")
    SdApiService apiService;
    @Resource(name = "userService")
    SdUserService userService;

    @Override
    protected SiteInterfaceBean getTargetApiSiteInterfaceBean() {
        return apiService.getSiteInterfaceBean(SiteInterfaceBean.API_WFM.API_NAME);
    }

    @Override
    protected Invocation.Builder getInvocationBuilder(WebTarget webTarget) {
        SiteInterfaceBean siteInterfaceBean = getTargetApiSiteInterfaceBean();
        return webTarget.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getBtuHeaderAuthKey(siteInterfaceBean));
    }

    @Override
    public Integer createJob(SdTicketData ticketData) {
        Entity<SdTicketData> postBody = Entity.entity(ticketData, MediaType.APPLICATION_JSON);

//        String jsonResponseString = postData("/api/v1/sd/FaultCreate", null, postBody);
//        if(StringUtils.isEmpty(jsonResponseString)) {
//            return null;
//        }
//
//        WfmResponseData wfmResponseData = new Gson().<WfmResponseData<WfmJobCreateResponseData>>fromJson(
//                jsonResponseString, new TypeToken<WfmResponseData<WfmJobCreateResponseData>>(){}.getType());
//        WfmJobCreateResponseData wfmJobCreateResponseData = wfmResponseData.getData();

        return Optional.ofNullable(postData("/api/v1/sd/FaultCreate", null, postBody)).flatMap(json ->
                Optional.ofNullable(new Gson().<WfmResponseData<WfmJobCreateResponseData>>fromJson(json, new TypeToken<WfmResponseData<WfmJobCreateResponseData>>() {
        }.getType())).map(WfmResponseData::getData).map(WfmJobCreateResponseData::getJobId)).orElse(0);
    }

    @Override
    public boolean closeTicket(Integer ticketMasId) {
        try {
            return Optional.ofNullable(ticketMasId).flatMap(id -> Optional.ofNullable(postData("/api/v1/sd/CloseTicket/" + id, null, null))
                    .filter(StringUtils::isNotBlank)
                    .map(s -> StringUtils.containsIgnoreCase(s, "Success"))).orElse(false);
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public WfmJobDetailsData getJobDetails(Integer jobId) {
        return Optional.ofNullable(jobId).map(id -> {
            Map<String, String> queryParamMap = new HashMap<>();
            queryParamMap.put("jobId", id.toString());
            String wfmResponseDataJsonString = getData("/api/v1/sd/FaultView", queryParamMap);
            WfmResponseData<WfmJobDetailsData> wfmResponseData = populateWfmResponseData(
                    wfmResponseDataJsonString, new TypeToken<WfmResponseData<WfmJobDetailsData>>() {
                    }.getType());
            return wfmResponseData.getData();
        }).orElse(new WfmJobDetailsData());
    }

    @Override
    public List<SdServiceTypeOfferMappingBean> getServiceTypeOfferMapping() {
        return Optional.ofNullable(new Gson().<List<WfmOfferNameProductTypeData>>fromJson(getData("/api/v1/sd/GetOfferNameProductTypeMapping", null),
                new TypeToken<List<WfmOfferNameProductTypeData>>() {
                }.getType()))
                .filter(CollectionUtils::isNotEmpty).map(list -> list.stream().map(data -> {
                    SdServiceTypeOfferMappingBean bean = new SdServiceTypeOfferMappingBean();
                    bean.setOfferName(data.getServiceName());
                    bean.setServiceTypeCode(data.getProductType());
                    return bean;
                }).collect(Collectors.toList())).orElseThrow(() -> new RuntimeException("Service type offer mapping not found."));
    }

    private <T extends DataInterface> WfmResponseData<T> populateWfmResponseData(String wfmResponseDataJsonString, Type type) {
        WfmResponseData<T> wfmResponseData = new Gson().fromJson(wfmResponseDataJsonString, type);
        Optional.ofNullable(wfmResponseData).ifPresent(data -> {
            LOG.info("Response type: " + data.getType());
            if (StringUtils.isNotEmpty(data.getErrorMsg())) {
                LOG.error("Response status: " + data.getCode());
                LOG.error("Error message: " + data.getErrorMsg());
            }
        });
        return wfmResponseData;
    }

    @Override
    public SdPendingOrderData getPendingOrderByBsn(String bsn) {
        SdPendingOrderData pendingOrderData = new SdPendingOrderData();
        String wfmResponseDataJsonString = null;
        try {
            wfmResponseDataJsonString = getData("/api/v1/sd/GetPendingOrderByBsn/" + bsn, null);
        } catch (RuntimeException e) {
            String errorMsg = "WFM Error: Cannot check pending order from WFM of BSN " + bsn + ".";
            LOG.error(errorMsg);
        }

        WfmPendingOrderData responseData = new Gson().fromJson(wfmResponseDataJsonString, new TypeToken<WfmPendingOrderData>() {
        }.getType());
        Long pendingOrderId = responseData==null ? null : responseData.getOrderId();
        if (pendingOrderId!=null && pendingOrderId!=0) {
//            String[] arr = responseDataList.toArray(new String[responseDataList.size()]);
            pendingOrderData.setPendingOrder(String.format("%d", pendingOrderId));
        }
        return pendingOrderData;
    }

    @Override
    public List<WfmJobData> getJobInfo(Integer ticketMasId) {
        return Optional.ofNullable(ticketMasId).map(id -> {
            List<WfmJobData> dataList = getDataListTest("/api/v1/sd/GetJobListByTicketId/" + ticketMasId, null);
            return dataList;
        }).orElse(null);
    }
}
