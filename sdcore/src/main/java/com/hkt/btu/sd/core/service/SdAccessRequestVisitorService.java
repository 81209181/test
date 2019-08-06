package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.sd.core.exception.AccessRequestVisitorNotFoundException;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.exception.CompanyNotFoundException;
import com.hkt.btu.sd.core.exception.InvalidWorkflowException;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestVisitorBean;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SdAccessRequestVisitorService {

    List<SdAccessRequestVisitorBean> getVisitorListByAccessRequestId(Integer accessRequestId) throws AuthorityNotFoundException;

    SdAccessRequestVisitorBean getSelfVisitVisitorBean() throws UserNotFoundException, CompanyNotFoundException;
    SdAccessRequestVisitorBean getSelfVisitVisitorBean(SdUserBean requesterUserBean, SdCompanyBean requesterCompanyBean);

    boolean isSameVisitorProfile(SdAccessRequestVisitorBean b1, SdAccessRequestVisitorBean b2);

    Page<SdAccessRequestVisitorBean> searchAccessRequest(
            Pageable pageable,
            String visitorName, String companyName,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo) throws AuthorityNotFoundException;

    void createVisitorForAccessRequest(List<SdAccessRequestVisitorBean> visitorBeanList, Integer newAccessRequestId, SdUserBean requesterUserBean);

    void checkin(Integer visitorAccessId, String visitorCardNum)
            throws AuthorityNotFoundException, AccessRequestVisitorNotFoundException, InvalidWorkflowException;
    void checkout(Integer visitorAccessId)
            throws AuthorityNotFoundException, AccessRequestVisitorNotFoundException, InvalidWorkflowException;
}
