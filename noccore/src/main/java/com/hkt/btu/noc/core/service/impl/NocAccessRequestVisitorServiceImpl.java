package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.noc.core.dao.entity.NocAccessRequestVisitorEntity;
import com.hkt.btu.noc.core.dao.mapper.NocAccessRequestVisitorMapper;
import com.hkt.btu.noc.core.dao.populator.NocAccessRequestVisitorEntityPopulator;
import com.hkt.btu.noc.core.exception.*;
import com.hkt.btu.noc.core.service.*;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestBean;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestVisitorBean;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import com.hkt.btu.noc.core.service.populator.NocAccessRequestVisitorBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NocAccessRequestVisitorServiceImpl implements NocAccessRequestVisitorService {
    private static final Logger LOG = LogManager.getLogger(NocAccessRequestVisitorServiceImpl.class);

    @Resource
    NocAccessRequestVisitorMapper nocAccessRequestVisitorMapper;

    @Resource(name = "userService")
    NocUserService nocUserService;
    @Resource(name = "companyService")
    NocCompanyService nocCompanyService;
    @Resource(name = "accessRequestService")
    NocAccessRequestService nocAccessRequestService;
    @Resource(name = "inputCheckService")
    NocInputCheckService nocInputCheckService;

    @Resource(name = "accessRequestVisitorEntityPopulator")
    NocAccessRequestVisitorEntityPopulator nocAccessRequestVisitorEntityPopulator;
    @Resource(name = "accessRequestVisitorBeanPopulator")
    NocAccessRequestVisitorBeanPopulator nocAccessRequestVisitorBeanPopulator;

    @Override
    public List<NocAccessRequestVisitorBean> getVisitorListByAccessRequestId(Integer accessRequestId) throws AuthorityNotFoundException {
        // determine company id restriction
        Integer companyId = nocUserService.getCompanyIdRestriction();
        // determine user id restriction
        Integer userId = nocUserService.getUserIdRestriction();

        // get
        List<NocAccessRequestVisitorEntity> visitorEntityList = nocAccessRequestVisitorMapper.getAccessRequestVisitorsByAccessRequestId(accessRequestId, companyId, userId);
        if(CollectionUtils.isEmpty(visitorEntityList)){
            return new ArrayList<>();
        }

        // populate
        List<NocAccessRequestVisitorBean> visitorBeanList = new ArrayList<>();
        for(NocAccessRequestVisitorEntity visitorEntity : visitorEntityList){
            NocAccessRequestVisitorBean visitorBean = new NocAccessRequestVisitorBean();
            nocAccessRequestVisitorBeanPopulator.populate(visitorEntity, visitorBean);
            visitorBeanList.add(visitorBean);
        }

        return visitorBeanList;
    }

    @Override
    public NocAccessRequestVisitorBean getSelfVisitVisitorBean() throws UserNotFoundException, CompanyNotFoundException {
        NocUserBean requesterUserBean = (NocUserBean) nocUserService.getCurrentUserBean();
        NocCompanyBean requesterCompanyBean = nocCompanyService.getCompanyById(requesterUserBean.getCompanyId());
        return getSelfVisitVisitorBean(requesterUserBean, requesterCompanyBean);
    }

    @Override
    public NocAccessRequestVisitorBean getSelfVisitVisitorBean(NocUserBean requesterUserBean, NocCompanyBean requesterCompanyBean) {
        NocAccessRequestVisitorBean visitorBean = new NocAccessRequestVisitorBean();
        nocAccessRequestVisitorBeanPopulator.populate(requesterUserBean, requesterCompanyBean, visitorBean);
        return visitorBean;
    }

    @Override
    public boolean isSameVisitorProfile(NocAccessRequestVisitorBean b1, NocAccessRequestVisitorBean b2) {
        if(b1==null && b2==null){
            return true;
        } else if (b1==null || b2==null){
            return false;
        }

        return StringUtils.equals(b1.getName(), b2.getName()) &&
                StringUtils.equals(b1.getCompanyName(), b2.getCompanyName()) &&
                StringUtils.equals(b1.getStaffId(), b2.getStaffId()) &&
                StringUtils.equals(b1.getMobile(), b2.getMobile());
    }

    @Override
    public Page<NocAccessRequestVisitorBean> searchAccessRequest(
            Pageable pageable,
            String visitorName, String companyName,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo) throws AuthorityNotFoundException {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // determine company id restriction
        Integer companyIdRestriction = nocUserService.getCompanyIdRestriction();

        // determine user id restriction
        Integer userIdRestriction = nocUserService.getUserIdRestriction();

        LOG.info( String.format(
                "Searching visitor with {companyId: %s, userId: %s, " +
                        "visitorName: %s, companyName: %s, visitLocation: %s, visitDateFrom: %s, visitDateTo: %s}",
                companyIdRestriction, userIdRestriction,
                visitorName, companyName, visitLocation, visitDateFrom, visitDateTo) );

        // get request total count
        Integer totalCount = nocAccessRequestVisitorMapper.countSearchVisitor(
                companyIdRestriction, userIdRestriction,
                visitorName, companyName, visitLocation, visitDateFrom, visitDateTo);
        // get request content
        List<NocAccessRequestVisitorEntity> entityList = nocAccessRequestVisitorMapper.searchVisitor(
                offset, pageSize, companyIdRestriction, userIdRestriction,
                visitorName, companyName, visitLocation, visitDateFrom, visitDateTo);
        if( CollectionUtils.isEmpty(entityList) ){
            return new PageImpl<>(new LinkedList<>(), pageable, totalCount);
        }

        List<NocAccessRequestVisitorBean> beanList = new LinkedList<>();
        for (NocAccessRequestVisitorEntity entity : entityList){
            NocAccessRequestVisitorBean bean = new NocAccessRequestVisitorBean();
            nocAccessRequestVisitorBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return new PageImpl<>(beanList, pageable, totalCount);
    }

    @Override
    @Transactional
    public void createVisitorForAccessRequest(List<NocAccessRequestVisitorBean> visitorBeanList, Integer newAccessRequestId, NocUserBean requesterUserBean) {
        // prepare entity
        List<NocAccessRequestVisitorEntity> visitorEntityList = new LinkedList<>();
        removeEmptyVisitors(visitorBeanList);

        if(CollectionUtils.isEmpty(visitorBeanList)){
            throw new InvalidInputException("Missing visitor input.");
        }else {
            for(NocAccessRequestVisitorBean visitorBean : visitorBeanList){
                checkValidVisitor(visitorBean);

                NocAccessRequestVisitorEntity visitorEntity = new NocAccessRequestVisitorEntity();
                nocAccessRequestVisitorEntityPopulator.populate(visitorBean, visitorEntity);
                nocAccessRequestVisitorEntityPopulator.populate(requesterUserBean, visitorEntity);
                visitorEntity.setAccessRequestId(newAccessRequestId);
                visitorEntityList.add(visitorEntity);
            }
        }

        // insert visitor
        if(!CollectionUtils.isEmpty(visitorEntityList)){
            nocAccessRequestVisitorMapper.insertVisitors(visitorEntityList);
        }
    }

    @Override
    @Transactional
    public void checkin(Integer visitorAccessId, String visitorCardNum)
            throws AuthorityNotFoundException, AccessRequestVisitorNotFoundException, InvalidWorkflowException, AccessRequestNotFoundException {
        // check authority
        if( ! nocUserService.isInternalUser() ){
            throw new AuthorityNotFoundException("Authority not found for checking in visitor.");
        }

        // check request
        NocAccessRequestBean nocAccessRequestBean = nocAccessRequestService.getAccessRequestByVisitorId(visitorAccessId);
        nocAccessRequestService.checkRequestNowEligibleForCheckInOut(nocAccessRequestBean);

        // update
        int updateCount = nocAccessRequestVisitorMapper.checkin(visitorAccessId, visitorCardNum);

        // check update
        if(updateCount==0){
            throw new AccessRequestVisitorNotFoundException("Cannot check-in visitorAccessId " + visitorAccessId + ".");
        }
    }

    @Override
    @Transactional
    public void checkout(Integer visitorAccessId)
            throws AuthorityNotFoundException, AccessRequestVisitorNotFoundException, InvalidWorkflowException, AccessRequestNotFoundException {
        // check authority
        if( ! nocUserService.isInternalUser() ){
            throw new AuthorityNotFoundException("Authority not found for checking out visitor.");
        }

        // check request
        NocAccessRequestBean nocAccessRequestBean = nocAccessRequestService.getAccessRequestByVisitorId(visitorAccessId);
        nocAccessRequestService.checkRequestNowEligibleForCheckInOut(nocAccessRequestBean);

        // update
        int updateCount = nocAccessRequestVisitorMapper.checkout(visitorAccessId);

        // check update
        if(updateCount==0){
            throw new AccessRequestVisitorNotFoundException("Cannot check-out visitorAccessId " + visitorAccessId + ".");
        }
    }

    private void removeEmptyVisitors(List<NocAccessRequestVisitorBean> requestVisitorBeanList){
        requestVisitorBeanList.removeIf(
                visitorBean -> visitorBean == null || (
                                StringUtils.isEmpty(visitorBean.getName()) &&
                                StringUtils.isEmpty(visitorBean.getCompanyName()) &&
                                StringUtils.isEmpty(visitorBean.getStaffId()) &&
                                StringUtils.isEmpty(visitorBean.getMobile())
                )
        );
    }

    private void checkValidVisitor(NocAccessRequestVisitorBean visitorBean) throws InvalidInputException {
        if(StringUtils.isEmpty(visitorBean.getName())){
            throw new InvalidInputException("Missing visitor's name.");
        }

        try {
            nocInputCheckService.checkName(visitorBean.getName());
            nocInputCheckService.checkCompanyName(visitorBean.getCompanyName());
            nocInputCheckService.checkStaffIdHkidPassport(visitorBean.getStaffId());
            nocInputCheckService.checkMobile(visitorBean.getMobile());
        } catch (InvalidInputException e){
            throw new InvalidInputException("Visitor " + visitorBean.getName() + ": " + e.getMessage());
        }
    }
}
