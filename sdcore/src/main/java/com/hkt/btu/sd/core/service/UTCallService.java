package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.UTCallRequestBean;
import com.hkt.btu.sd.core.service.bean.UTCallPageBean;

import java.util.List;
import java.util.Map;

public interface UTCallService {
    void insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg,
                               String serviceCode, String seq, String seqType);
    void insertNewUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary);
    void updateUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary);
    void updateRequestAfterGetResult(String utCallId);
    boolean utCallResultRecordExist(String utCallId);
    List<UTCallPageBean> getUTCallRequestRecordList();
}
