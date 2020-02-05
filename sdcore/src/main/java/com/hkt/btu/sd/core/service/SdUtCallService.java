package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdUtCallPageBean;

import java.util.List;
import java.util.Map;

public interface SdUtCallService {
    void insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg,
                               String serviceCode, String seq, String seqType, Integer ticketDetId);
    void insertNewUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary);
    void updateUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary);
    void updateRequestAfterGetResult(String utCallId);
    boolean utCallResultRecordExist(String utCallId);
    List<SdUtCallPageBean> getUTCallRequestRecordList();
}
