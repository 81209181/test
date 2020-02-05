package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.service.SdUtCallService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.dao.mapper.UTCallRequestRecordMapper;
import com.hkt.btu.sd.core.dao.mapper.UtCallResultRecordMapper;
import com.hkt.btu.sd.core.dao.mapper.UTCallMapper;
//import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.dao.entity.UTCallPageEntity;
import com.hkt.btu.sd.core.service.bean.UTCallPageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.sd.core.service.populator.UTCallRequestBeanPopulator;
import com.hkt.btu.sd.core.service.populator.UTCallPageBeanPopulator;

import java.util.ArrayList;
import javax.annotation.Resource;
import java.util.*;

public class SdUtCallServiceImpl implements SdUtCallService {

    //other services
    @Resource(name = "userService")
    SdUserService sdUserService;

    //mappers
    @Resource
    UTCallRequestRecordMapper utCallRequestRecordMapper;
    @Resource
    UtCallResultRecordMapper utCallResultRecordMapper;
    @Resource
    UTCallMapper utCallMapper;

    //populators
    @Resource(name = "callRequestBeanPopulator")
    UTCallRequestBeanPopulator utCallRequestBeanPopulator;
    @Resource(name = "callPageBeanPopulator")
    UTCallPageBeanPopulator utCallPageBeanPopulator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg, String serviceCode, String seq, String seqType, Integer ticketDetId){
        if (StringUtils.isEmpty(triggerNewBSNNum)) {
            throw new InvalidInputException("bsn number not found.");
        }

        String createBy = null;
        createBy = sdUserService.getCurrentUserUserId();

        if (createBy==null){
            throw new UserNotFoundException("Cannot find user id");
        }

        utCallRequestRecordMapper.insertNewUTCallRequestRecord(triggerNewBSNNum, code, msg, serviceCode, seq, seqType, ticketDetId, createBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertNewUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary){
        if (StringUtils.isEmpty(utCallId)) {
            throw new InvalidInputException("utCallId not found.");
        }

        String createBy = null;
        createBy = sdUserService.getCurrentUserUserId();

        if (createBy==null){
            throw new UserNotFoundException("Cannot find user id");
        }

        //change the utSummary to String
        String utSummaryString = getUTSummaryString(utSummary);

        utCallResultRecordMapper.insertNewUTCallResultRecord(utCallId, code, msg, utSummaryString, createBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary){
        if (StringUtils.isEmpty(utCallId)) {
            throw new InvalidInputException("utCallId not found.");
        }

        String modifyBy = null;
        modifyBy = sdUserService.getCurrentUserUserId();

        if (modifyBy==null){
            throw new UserNotFoundException("Cannot find user id");
        }

        //change the utSummary to String
        String utSummaryString = getUTSummaryString(utSummary);

        utCallResultRecordMapper.updateUTCallResultRecord(utCallId, code, msg, utSummaryString, modifyBy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRequestAfterGetResult(String utCallId){
        if (StringUtils.isEmpty(utCallId)) {
            throw new InvalidInputException("utCallId not found.");
        }

        String modifyBy = null;
        modifyBy = sdUserService.getCurrentUserUserId();

        if (modifyBy==null){
            throw new UserNotFoundException("Cannot find user id");
        }

        //change the utCallId String to Integer
        Integer utCallIdInt = null;
        try {
            utCallIdInt = Integer.parseInt(utCallId);
        }
        catch (NumberFormatException e){
            throw new InvalidInputException("utCallId is not integer.");
        }

        if (utCallIdInt!=null){
            utCallRequestRecordMapper.updateRequestAfterGetResult(utCallIdInt, modifyBy);
        }
    }

    @Override
    public boolean utCallResultRecordExist(String utCallId){
        if(utCallResultRecordMapper.utCallResultRecordExist(utCallId)>0){
            return true;
        }
        return false;
    }

    @Override
    public List<UTCallPageBean> getUTCallRequestRecordList(){
        List<UTCallPageBean> utCallRecordListBean = new ArrayList();
        List<UTCallPageEntity> utCallRecordList = utCallMapper.getUTCallRecord();

        for (UTCallPageEntity entity : utCallRecordList){
            UTCallPageBean bean = new UTCallPageBean();
            utCallPageBeanPopulator.populate(entity, bean);
            utCallRecordListBean.add(bean);
        }

        return utCallRecordListBean;
    }

    private String getUTSummaryString(List<Map<String, String>> utSummary){
        if (utSummary==null)
            return null;

        StringBuilder utSummaryString = new StringBuilder("[");
        for (Map<String, String> map : utSummary){
            StringBuilder mapAsString = new StringBuilder("{");
            for (String key : map.keySet()){
                mapAsString.append(key + "=" + map.get(key) + ", ");
            }
            mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");

            utSummaryString.append(mapAsString + ", ");
        }
        utSummaryString.delete(utSummaryString.length()-2, utSummaryString.length()).append("]");

        return utSummaryString.toString();
    }
}