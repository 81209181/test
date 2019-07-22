package com.hkt.btu.noc.core.service;


import com.hkt.btu.noc.core.exception.*;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestBean;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestLocBean;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestVisitorBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public interface NocAccessRequestService {
    Integer createAccessRequest(NocAccessRequestBean nocAccessRequestBean)
            throws UserNotFoundException, CompanyNotFoundException, InvalidInputException, LocationNotFoundException;
    void sendAccessRequestCfmEmail(Integer newAccessRequestId) throws MessagingException, AccessRequestNotFoundException;

    NocAccessRequestBean getAccessRequestByRequestId(Integer accessRequestId) throws AuthorityNotFoundException;
    NocAccessRequestBean getAccessRequestByVisitorId(Integer visitorAccessId) throws AuthorityNotFoundException;
    NocAccessRequestBean getAccessRequestBasicInfoByRequestId(Integer accessRequestId) throws AuthorityNotFoundException;

    LinkedList<String> getAccessRequestStatusList();
    List<NocAccessRequestLocBean> getAccessRequestLocList();

    void checkRequestNowEligibleForCheckInOut(NocAccessRequestBean nocAccessRequestBean) throws InvalidWorkflowException, AccessRequestNotFoundException;

    Page<NocAccessRequestBean> searchAccessRequest(Pageable pageable,
                                                   Integer accessRequestId, String companyName, String status,
                                                   String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo)
            throws AuthorityNotFoundException;

    boolean canOnlySubmitSelfVisit();
    NocAccessRequestVisitorBean getSelfVisitVisitorBean();

    // jobs
    void completeExpiredAccessRequest();
}
