package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.UTCallService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import com.hkt.btu.sd.core.service.bean.UTCallRequestBean;
import com.hkt.btu.sd.core.dao.entity.UTCallPageEntity;
import com.hkt.btu.sd.core.service.bean.UTCallPageBean;
import com.hkt.btu.sd.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.facade.UTCallFacade;
import com.hkt.btu.sd.facade.data.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.sd.facade.populator.UTCallPageDataPopulator;

import javax.annotation.Resource;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class UTCallFacadeImpl extends AbstractRestfulApiFacade implements UTCallFacade{
    private static final Logger LOG = LogManager.getLogger(UTCallFacadeImpl.class);

    //services
    @Resource(name = "apiService")
    SdApiService apiService;
    @Resource(name = "utCallService")
    UTCallService utCallService;

    //populators
    @Resource(name = "callPageDataPopulator")
    UTCallPageDataPopulator utCallPageDataPopulator;

    @Override
    protected SiteInterfaceBean getTargetApiSiteInterfaceBean() {
        return apiService.getSiteInterfaceBean(SiteInterfaceBean.API_UT_CALL.API_NAME);
    }

    @Override
    protected Invocation.Builder getInvocationBuilder(WebTarget webTarget) {
        SiteInterfaceBean siteInterfaceBean = getTargetApiSiteInterfaceBean();
        webTarget = webTarget.queryParam("fid", siteInterfaceBean.getBeId());
        webTarget = webTarget.queryParam("user", siteInterfaceBean.getUserName());
        webTarget = webTarget.queryParam("pwd", siteInterfaceBean.getPassword());

        webTarget = webTarget.queryParam("sys", siteInterfaceBean.getSystemName());

        webTarget = webTarget.queryParam("seq", "2");
        webTarget = webTarget.queryParam("type", siteInterfaceBean.getChannelType());
        return webTarget.request(MediaType.APPLICATION_JSON);
    }

    //own implementation

    @Override
    public String newUtCallRequest(String triggerNewBSNNum){
        UTCallRequestTempData requestData = triggerNewUTCall(triggerNewBSNNum);
        return insertNewUTCallRequestRecord(triggerNewBSNNum, requestData.getCODE(), requestData.getMSG(), requestData.getSERVICECODE(), requestData.getSEQ(), requestData.getSEQTYPE());
    }

    private UTCallRequestTempData triggerNewUTCall(String BSNNum){
        String apiPath = "/utapi/enquiry";

        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("xid", BSNNum);

        return getData(apiPath, UTCallRequestTempData.class, queryParamMap);
    }

    private String insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg, String serviceCode, String seq, String seqType){
        try{
            utCallService.insertNewUTCallRequestRecord(triggerNewBSNNum, code, msg, serviceCode, seq, seqType);
        }
        catch (InvalidInputException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
        catch (UserNotFoundException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
        catch (Exception e){
            LOG.warn(e.getMessage());
            return "Unhandle Error Occur: Please see the log for detail";
        }
        return null;
    }

    @Override
    public String newUtCallResult(String utCallId, String serviceCode){
        UTCallProgressData resultData = checkNewUTCallProgress(serviceCode);
        String resultMsg = insertNewUTCallResultRecord(utCallId, resultData.getCODE(), resultData.getMSG(), resultData.getACTIONDATA().getUT_SUMMARY());

        if (resultMsg==null){
            String updateMsg = updateRequestAfterGetResult(utCallId);
            if (updateMsg==null){
                return null;
            }
            else {
                return "update request failed.";
            }
        }
        return "get result failed.";
    }

    private UTCallProgressData checkNewUTCallProgress(String serviceCode){
        String apiPath = "/utapi/getresultt4";

        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("servicecode", serviceCode);

        return getData(apiPath, UTCallProgressData.class, queryParamMap);
    }

    private String insertNewUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary){
        try{
            if(utCallService.utCallResultRecordExist(utCallId)){
                utCallService.updateUTCallResultRecord(utCallId, code, msg, utSummary);
            }
            else {
                utCallService.insertNewUTCallResultRecord(utCallId, code, msg, utSummary);
            }
        }
        catch (InvalidInputException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
        catch (UserNotFoundException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
        catch (Exception e){
            LOG.warn(e.getMessage());
            return "Unhandle Error Occur: Please see the log for detail";
        }
        return null;
    }

    private String updateRequestAfterGetResult(String utCallId){
        try{
            utCallService.updateRequestAfterGetResult(utCallId);
        }
        catch (InvalidInputException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
        catch (UserNotFoundException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
        catch (Exception e){
            LOG.warn(e.getMessage());
            return "Unhandle Error Occur: Please see the log for detail";
        }
        return null;
    }

    @Override
    public List<UTCallPageData> getUTCallRequestRecordList(){
        List<UTCallPageData> utCallRecordListData = new ArrayList();
        List<UTCallPageBean> utCallRecordList = utCallService.getUTCallRequestRecordList();

        for (UTCallPageBean bean : utCallRecordList){
            UTCallPageData data = new UTCallPageData();
            utCallPageDataPopulator.populate(bean, data);
            utCallRecordListData.add(data);
        }

        return utCallRecordListData;
    }
}