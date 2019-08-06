package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public interface SdAccessRequestFacade {

    boolean canOnlySubmitSelfVisit();

    void getPrefillAccessFormData(CreateAccessRequestFormData createAccessRequestFormData);
    void copyAccessFormData(SdAccessRequestData sdAccessRequestData, CreateAccessRequestFormData createAccessRequestFormData);

    SdAccessRequestData getAccessRequestByRequestId(Integer hashedRequestId);
    SdAccessRequestData getAccessRequestBasicInfoByRequestId(Integer hashedRequestId);
    List<SdAccessRequestVisitorData> getAccessRequestVisitorListByRequestId(Integer hashedRequestId);
    List<SdAccessRequestEquipData> getAccessRequestEquipListByRequestId(Integer hashedRequestId);

    LinkedList<String> getAccessRequestStatusList();
    TreeMap<String, SdAccessRequestLocData> getAccessRequestLocMap();

    PageData<SdAccessRequestData> searchAccessRequest(
            Pageable pageable, Integer hashedRequestId,
            String companyName, String status,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo);
    PageData<SdAccessRequestVisitorData> searchAccessRequestVisitor(
            Pageable pageable,
            String visitorName, String companyName,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo);

    List<SdAccessRequestData> getPendingAccessRequest(String targetLocation);
    List<SdAccessRequestData> getRecentCompletedAccessRequest(String targetLocation);

    CreateResultData createAccessRequest(CreateAccessRequestFormData accessFormData);

    String checkinAccessRequestVisitor(Integer visitorAccessId, String visitorCardNum);
    String checkoutAccessRequestVisitor(Integer visitorAccessId);

    List<SdOperationHistData> getOperationHistList(Integer hashedRequestId);

}
