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
        return webTarget.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, siteInterfaceBean.getPassword());
//                .header(SiteInterfaceBean.API_UT_CALL.API_HEADER.OPERATOR_ID,   siteInterfaceBean.getUserName())
//                .header(SiteInterfaceBean.API_UT_CALL.API_HEADER.CHANNEL_TYPE,  siteInterfaceBean.getChannelType())
//                .header(SiteInterfaceBean.API_UT_CALL.API_HEADER.F_ID,          siteInterfaceBean.getBeId())
//                .header(SiteInterfaceBean.API_UT_CALL.API_HEADER.X_APP_KEY,     siteInterfaceBean.getxAppkey());
    }

    //own implementation

    @Override
    public UTCallRequestTempData triggerNewUTCall(String BSNNum){
        String apiPath = "/utapi/enquiry";

        SiteInterfaceBean siteInterfaceBean = getTargetApiSiteInterfaceBean();

        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("fid", siteInterfaceBean.getBeId());
        queryParamMap.put("user", siteInterfaceBean.getUserName());
        queryParamMap.put("pwd", "nvng[kGZE\\C^");
        queryParamMap.put("xid", BSNNum);
        queryParamMap.put("sys", siteInterfaceBean.getSystemName());

        return getData(apiPath, UTCallRequestTempData.class, queryParamMap);
    }

    @Override
    public String insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg, String serviceCode, String seq, String seqType){
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
    public UTCallProgressData checkNewUTCallProgress(String serviceCode, String seq){
        String apiPath = "/utapi/getresultt4";

        SiteInterfaceBean siteInterfaceBean = getTargetApiSiteInterfaceBean();

        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("servicecode", serviceCode);
        queryParamMap.put("seq", seq);
        queryParamMap.put("sys", siteInterfaceBean.getSystemName());
        queryParamMap.put("type", siteInterfaceBean.getChannelType());

        UTCallProgressData result = getData(apiPath, UTCallProgressData.class, queryParamMap);

        return result;
    }

    @Override
    public String insertNewUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary){
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

    @Override
    public String updateRequestAfterGetResult(String utCallId){
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