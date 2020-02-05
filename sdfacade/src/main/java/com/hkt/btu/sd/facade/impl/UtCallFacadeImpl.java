package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.facade.AbstractRestfulApiFacade;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.core.service.UTCallService;
import com.hkt.btu.sd.core.service.bean.SdApiProfileBean;
import com.hkt.btu.sd.core.service.bean.UTCallPageBean;
import com.hkt.btu.sd.facade.UtCallFacade;
import com.hkt.btu.sd.facade.data.UTCallPageData;
import com.hkt.btu.sd.facade.data.UTCallRequestTempData;
import com.hkt.btu.sd.facade.data.ut.UTCallProgressData;
import com.hkt.btu.sd.facade.populator.UTCallPageDataPopulator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtCallFacadeImpl extends AbstractRestfulApiFacade implements UtCallFacade {
    private static final Logger LOG = LogManager.getLogger(UtCallFacadeImpl.class);

    @Resource(name = "apiService")
    SdApiService apiService;
    @Resource(name = "utCallService")
    UTCallService utCallService;

    @Resource(name = "callPageDataPopulator")
    UTCallPageDataPopulator utCallPageDataPopulator;

    @Override
    protected SdApiProfileBean getTargetApiProfile() {
        return apiService.getUtApiProfileBean();
    }

    @Override
    public String newUtCallRequest(String triggerNewBSNNum, Integer ticketDetId){
        UTCallRequestTempData requestData = triggerNewUTCall(triggerNewBSNNum);
        return insertNewUTCallRequestRecord(triggerNewBSNNum, requestData.getCODE(), requestData.getMSG(), requestData.getSERVICECODE(), requestData.getSEQ(), requestData.getSEQTYPE(), ticketDetId);
    }

    private UTCallRequestTempData triggerNewUTCall(String BSNNum){
        String apiPath = "/utapi/enquiry";

        // prepare common query param
        SdApiProfileBean apiProfileBean = getTargetApiProfile();
        MultivaluedMap<String, Object> otherParamMap = apiProfileBean.getOtherParamMap();
        String fid = otherParamMap.getFirst(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.FID).toString();
        String user = otherParamMap.getFirst(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.USER).toString();
        String pwd = otherParamMap.getFirst(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.PWD).toString();
        String sys = otherParamMap.getFirst(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.SYS).toString();

        // add query param
        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.FID, fid);
        queryParamMap.put(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.USER, user);
        queryParamMap.put(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.PWD, pwd);
        queryParamMap.put(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.SYS, sys);
        queryParamMap.put(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.XID, BSNNum);

        return getData(apiPath, UTCallRequestTempData.class, queryParamMap);
    }

    private String insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg, String serviceCode, String seq, String seqType, Integer ticketDetId){
        try{
            utCallService.insertNewUTCallRequestRecord(triggerNewBSNNum, code, msg, serviceCode, seq, seqType, ticketDetId);
        }
        catch (InvalidInputException | UserNotFoundException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
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

        // prepare common query param
        SdApiProfileBean apiProfileBean = getTargetApiProfile();
        MultivaluedMap<String, Object> otherParamMap = apiProfileBean.getOtherParamMap();
        String seq = otherParamMap.getFirst(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.SEQ).toString();
        String sys = otherParamMap.getFirst(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.SYS).toString();
        String type = otherParamMap.getFirst(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.TYPE).toString();

        // add query param
        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.SEQ, seq);
        queryParamMap.put(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.SYS, sys);
        queryParamMap.put(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.TYPE, type);
        queryParamMap.put(SdApiProfileBean.API_UT_CALL.QUERY_PARAM.SERVICE_CODE, serviceCode);

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
        catch (InvalidInputException | UserNotFoundException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
        return null;
    }

    private String updateRequestAfterGetResult(String utCallId){
        try{
            utCallService.updateRequestAfterGetResult(utCallId);
        }
        catch (InvalidInputException | UserNotFoundException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
        return null;
    }

    @Override
    public List<UTCallPageData> getUTCallRequestRecordList(){
        List<UTCallPageData> utCallRecordListData = new ArrayList<>();
        List<UTCallPageBean> utCallRecordList = utCallService.getUTCallRequestRecordList();

        for (UTCallPageBean bean : utCallRecordList){
            UTCallPageData data = new UTCallPageData();
            utCallPageDataPopulator.populate(bean, data);
            utCallRecordListData.add(data);
        }

        return utCallRecordListData;
    }
}