package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.service.UTCallService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.dao.mapper.UTCallRequestRecordMapper;
import com.hkt.btu.sd.core.dao.mapper.UTCallResultRecordMapper;
//import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.dao.entity.UTCallRequestEntity;
import com.hkt.btu.sd.core.service.bean.UTCallRequestBean;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.sd.core.service.populator.UTCallRequestBeanPopulator;

import java.util.ArrayList;
import javax.annotation.Resource;
import java.util.*;

public class UTCallServiceImpl implements UTCallService {

    //other services
    @Resource(name = "userService")
    SdUserService sdUserService;

    //mappers
    @Resource
    UTCallRequestRecordMapper utCallRequestRecordMapper;
    @Resource
    UTCallResultRecordMapper utCallResultRecordMapper;

    //populators
    @Resource(name = "callRequestBeanPopulator")
    UTCallRequestBeanPopulator utCallRequestBeanPopulator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg, String serviceCode, String seq, String seqType){
        if (StringUtils.isEmpty(triggerNewBSNNum)) {
            throw new InvalidInputException("bsn number not found.");
        }

        String createBy = null;
        createBy = sdUserService.getCurrentUserUserId();

        if (createBy==null){
            throw new UserNotFoundException("Cannot find user id");
        }

        utCallRequestRecordMapper.insertNewUTCallRequestRecord(triggerNewBSNNum, code, msg, serviceCode, seq, seqType, createBy);
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
        String utSummaryString = "";

        utCallResultRecordMapper.insertNewUTCallResultRecord(utCallId, code, msg, utSummaryString, createBy);
    }

    @Override
    public List<UTCallRequestBean> getUTCallRequestRecordList(){
        List<UTCallRequestBean> utCallRecordListBean = new ArrayList();
        List<UTCallRequestEntity> utCallRecordList = utCallRequestRecordMapper.getUTCallRequestRecordList();

        for (UTCallRequestEntity entity : utCallRecordList){
            UTCallRequestBean bean = new UTCallRequestBean();
            utCallRequestBeanPopulator.populate(entity, bean);
            utCallRecordListBean.add(bean);
        }

        return utCallRecordListBean;
    }
}