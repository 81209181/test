package com.hkt.btu.noc.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.noc.facade.data.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public interface NocAccessRequestFacade {

    boolean canOnlySubmitSelfVisit();

    void getPrefillAccessFormData(CreateAccessRequestFormData createAccessRequestFormData);
    void copyAccessFormData(NocAccessRequestData nocAccessRequestData, CreateAccessRequestFormData createAccessRequestFormData);

    NocAccessRequestData getAccessRequestByRequestId(Integer hashedRequestId);
    NocAccessRequestData getAccessRequestBasicInfoByRequestId(Integer hashedRequestId);
    List<NocAccessRequestVisitorData> getAccessRequestVisitorListByRequestId(Integer hashedRequestId);
    List<NocAccessRequestEquipData> getAccessRequestEquipListByRequestId(Integer hashedRequestId);

    LinkedList<String> getAccessRequestStatusList();
    TreeMap<String, NocAccessRequestLocData> getAccessRequestLocMap();

    PageData<NocAccessRequestData> searchAccessRequest(
            Pageable pageable, Integer hashedRequestId,
            String companyName, String status,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo);
    PageData<NocAccessRequestVisitorData> searchAccessRequestVisitor(
            Pageable pageable,
            String visitorName, String companyName,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo);

    List<NocAccessRequestData> getPendingAccessRequest(String targetLocation);
    List<NocAccessRequestData> getRecentCompletedAccessRequest(String targetLocation);

    CreateResultData createAccessRequest(CreateAccessRequestFormData accessFormData);

    String checkinAccessRequestVisitor(Integer visitorAccessId, String visitorCardNum);
    String checkoutAccessRequestVisitor(Integer visitorAccessId);

    List<NocOperationHistData> getOperationHistList(Integer hashedRequestId);

}
