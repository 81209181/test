package com.hkt.btu.noc.core.service;

import com.hkt.btu.noc.core.exception.*;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestVisitorBean;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface NocAccessRequestVisitorService {

    List<NocAccessRequestVisitorBean> getVisitorListByAccessRequestId(Integer accessRequestId) throws AuthorityNotFoundException;

    NocAccessRequestVisitorBean getSelfVisitVisitorBean() throws UserNotFoundException, CompanyNotFoundException;
    NocAccessRequestVisitorBean getSelfVisitVisitorBean(NocUserBean requesterUserBean, NocCompanyBean requesterCompanyBean);

    boolean isSameVisitorProfile(NocAccessRequestVisitorBean b1, NocAccessRequestVisitorBean b2);

    Page<NocAccessRequestVisitorBean> searchAccessRequest(
            Pageable pageable,
            String visitorName, String companyName,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo) throws AuthorityNotFoundException;

    void createVisitorForAccessRequest(List<NocAccessRequestVisitorBean> visitorBeanList, Integer newAccessRequestId, NocUserBean requesterUserBean);

    void checkin(Integer visitorAccessId, String visitorCardNum)
            throws AuthorityNotFoundException, AccessRequestVisitorNotFoundException, InvalidWorkflowException;
    void checkout(Integer visitorAccessId)
            throws AuthorityNotFoundException, AccessRequestVisitorNotFoundException, InvalidWorkflowException;
}
