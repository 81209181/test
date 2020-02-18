package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.*;

import java.util.List;

public interface UtApiFacade {
    String newUtCallRequest(String triggerNewBSNNum, Integer ticketDetId);
//    UTCallRequestTempData triggerNewUTCall(String BSNNum);
//    String insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg,
//                                                String serviceCode, String seq, String seqType);

    String newUtCallResult(String utCallId, String serviceCode);
//    UTCallProgressData checkNewUTCallProgress(String serviceCode, String seq);
//    String insertNewUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary);
//    String updateRequestAfterGetResult(String utCallId);

    List<SdUtCallPageData> getUTCallRequestRecordList();

    List<SdUtCallPageData> getUTCallRequestRecordListByBsnNum(String bsnNum);
}