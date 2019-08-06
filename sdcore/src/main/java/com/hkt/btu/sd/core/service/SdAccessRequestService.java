package com.hkt.btu.sd.core.service;


import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestBean;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestLocBean;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestVisitorBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public interface SdAccessRequestService {
    Integer createAccessRequest(SdAccessRequestBean sdAccessRequestBean)
            throws UserNotFoundException, CompanyNotFoundException, InvalidInputException, LocationNotFoundException;
    void sendAccessRequestCfmEmail(Integer newAccessRequestId) throws MessagingException, AccessRequestNotFoundException;

    SdAccessRequestBean getAccessRequestByRequestId(Integer accessRequestId) throws AuthorityNotFoundException;
    SdAccessRequestBean getAccessRequestByVisitorId(Integer visitorAccessId) throws AuthorityNotFoundException;
    SdAccessRequestBean getAccessRequestBasicInfoByRequestId(Integer accessRequestId) throws AuthorityNotFoundException;

    LinkedList<String> getAccessRequestStatusList();
    List<SdAccessRequestLocBean> getAccessRequestLocList();

    void checkRequestNowEligibleForCheckInOut(SdAccessRequestBean sdAccessRequestBean) throws InvalidWorkflowException, AccessRequestNotFoundException;

    Page<SdAccessRequestBean> searchAccessRequest(Pageable pageable,
                                                   Integer accessRequestId, String companyName, String status,
                                                   String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo)
            throws AuthorityNotFoundException;

    boolean canOnlySubmitSelfVisit();
    SdAccessRequestVisitorBean getSelfVisitVisitorBean();

    // jobs
    void completeExpiredAccessRequest();
}
