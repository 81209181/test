package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.UTCallRequestBean;

import java.util.List;
import java.util.Map;

public interface UTCallService {
    void insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg,
                               String serviceCode, String seq, String seqType);
    void insertNewUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary);
    List<UTCallRequestBean> getUTCallRequestRecordList();
}
