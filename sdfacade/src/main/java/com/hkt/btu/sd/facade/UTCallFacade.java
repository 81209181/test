package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.data.*;

import java.util.List;
import java.util.Map;

public interface UTCallFacade {
    UTCallRequestTempData triggerNewUTCall(String BSNNum);
    String insertNewUTCallRequestRecord(String triggerNewBSNNum, String code, String msg,
                                                String serviceCode, String seq, String seqType);

    UTCallProgressData checkNewUTCallProgress(String serviceCode, String seq);
    String insertNewUTCallResultRecord(String utCallId, String code, String msg, List<Map<String, String>> utSummary);

    List<UTCallPageData> getUTCallRequestRecordList();
}