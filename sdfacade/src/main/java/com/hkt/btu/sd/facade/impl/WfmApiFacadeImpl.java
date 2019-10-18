package com.hkt.btu.sd.facade.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkt.btu.common.facade.data.DataInterface;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeOfferMappingBean;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.WfmApiFacade;
import com.hkt.btu.sd.facade.data.*;
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

public class WfmApiFacadeImpl extends AbstractRestfulApiFacade implements WfmApiFacade {
    private static final Logger LOG = LogManager.getLogger(WfmApiFacadeImpl.class);

    @Resource(name = "apiService")
    SdApiService apiService;

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
    public Integer createJob(WfmRequestDetailsBeanDate wfmRequestDetailsBeanDate, String createdBy) {
        WfmCreateSdFaultReqData wfmCreateSdFaultReqData = new WfmCreateSdFaultReqData();
        wfmCreateSdFaultReqData.setRequestDetailsBean(wfmRequestDetailsBeanDate);
        wfmCreateSdFaultReqData.setStaffId(createdBy);
        Entity<WfmCreateSdFaultReqData> postBody = Entity.entity(wfmCreateSdFaultReqData, MediaType.APPLICATION_JSON);
        String wfmResponseDataJsonString = postData("/api/v1/sd/FaultCreate", null, postBody);
        WfmResponseData<WfmJobCreateResponseData> wfmResponseData = populateWfmResponseData(
                wfmResponseDataJsonString, new TypeToken<WfmResponseData<WfmJobCreateResponseData>>() {
                }.getType());
        return Optional.ofNullable(wfmResponseData)
                .map(WfmResponseData::getData)
                .map(WfmJobCreateResponseData::getJobId)
                .orElse(1137876);   //demo 1137876
    }

    @Override
    public Integer completeJob(Integer jobId, String completeDateTime, String remark, String createdBy) {
        return Optional.ofNullable(jobId).map(id -> {
            WfmSdJobCompleteBeanData wfmSdJobCompleteBeanData = new WfmSdJobCompleteBeanData();
            wfmSdJobCompleteBeanData.setJobId(id);
            wfmSdJobCompleteBeanData.setCompleteDateTime(completeDateTime);
            wfmSdJobCompleteBeanData.setRemark(StringUtils.isEmpty(remark) ? StringUtils.EMPTY : remark);
            wfmSdJobCompleteBeanData.setStaffId(StringUtils.isEmpty(createdBy) ? StringUtils.EMPTY : createdBy);
            Entity<WfmSdJobCompleteBeanData> postBody = Entity.entity(wfmSdJobCompleteBeanData, MediaType.APPLICATION_JSON);
            WfmOrderResponseData wfmOrderResponseData = postData("/api/v1/sd/CompleteJob", WfmOrderResponseData.class, null, postBody);
            return Optional.ofNullable(wfmOrderResponseData)
                    .filter(responseData -> StringUtils.equals(responseData.getResultCode(), "0"))
                    .map(responseData -> id).orElse(0);
        }).orElse(0);
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
        return Optional.ofNullable(getData("/api/v1/sd/GetServiceTypeOfferMapping", null)).map(s -> {
            List<SdServiceTypeOfferMappingBean> list = new ArrayList<>();
            return list;
        }).orElseThrow(() -> new RuntimeException("Service type offer mapping not found."));
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
}
