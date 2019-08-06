package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.sd.core.dao.entity.SdAccessRequestVisitorEntity;
import com.hkt.btu.sd.core.dao.mapper.SdAccessRequestVisitorMapper;
import com.hkt.btu.sd.core.dao.populator.SdAccessRequestVisitorEntityPopulator;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.*;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestBean;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestVisitorBean;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import com.hkt.btu.sd.core.service.populator.SdAccessRequestVisitorBeanPopulator;
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

public class SdAccessRequestVisitorServiceImpl implements SdAccessRequestVisitorService {
    private static final Logger LOG = LogManager.getLogger(SdAccessRequestVisitorServiceImpl.class);

    @Resource
    SdAccessRequestVisitorMapper sdAccessRequestVisitorMapper;

    @Resource(name = "userService")
    SdUserService sdUserService;
    @Resource(name = "companyService")
    SdCompanyService sdCompanyService;
    @Resource(name = "accessRequestService")
    SdAccessRequestService sdAccessRequestService;
    @Resource(name = "inputCheckService")
    SdInputCheckService sdInputCheckService;

    @Resource(name = "accessRequestVisitorEntityPopulator")
    SdAccessRequestVisitorEntityPopulator sdAccessRequestVisitorEntityPopulator;
    @Resource(name = "accessRequestVisitorBeanPopulator")
    SdAccessRequestVisitorBeanPopulator sdAccessRequestVisitorBeanPopulator;

    @Override
    public List<SdAccessRequestVisitorBean> getVisitorListByAccessRequestId(Integer accessRequestId) throws AuthorityNotFoundException {
        // determine company id restriction
        Integer companyId = sdUserService.getCompanyIdRestriction();
        // determine user id restriction
        Integer userId = sdUserService.getUserIdRestriction();

        // get
        List<SdAccessRequestVisitorEntity> visitorEntityList = sdAccessRequestVisitorMapper.getAccessRequestVisitorsByAccessRequestId(accessRequestId, companyId, userId);
        if(CollectionUtils.isEmpty(visitorEntityList)){
            return new ArrayList<>();
        }

        // populate
        List<SdAccessRequestVisitorBean> visitorBeanList = new ArrayList<>();
        for(SdAccessRequestVisitorEntity visitorEntity : visitorEntityList){
            SdAccessRequestVisitorBean visitorBean = new SdAccessRequestVisitorBean();
            sdAccessRequestVisitorBeanPopulator.populate(visitorEntity, visitorBean);
            visitorBeanList.add(visitorBean);
        }

        return visitorBeanList;
    }

    @Override
    public SdAccessRequestVisitorBean getSelfVisitVisitorBean() throws UserNotFoundException, CompanyNotFoundException {
        SdUserBean requesterUserBean = (SdUserBean) sdUserService.getCurrentUserBean();
        SdCompanyBean requesterCompanyBean = sdCompanyService.getCompanyById(requesterUserBean.getCompanyId());
        return getSelfVisitVisitorBean(requesterUserBean, requesterCompanyBean);
    }

    @Override
    public SdAccessRequestVisitorBean getSelfVisitVisitorBean(SdUserBean requesterUserBean, SdCompanyBean requesterCompanyBean) {
        SdAccessRequestVisitorBean visitorBean = new SdAccessRequestVisitorBean();
        sdAccessRequestVisitorBeanPopulator.populate(requesterUserBean, requesterCompanyBean, visitorBean);
        return visitorBean;
    }

    @Override
    public boolean isSameVisitorProfile(SdAccessRequestVisitorBean b1, SdAccessRequestVisitorBean b2) {
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
    public Page<SdAccessRequestVisitorBean> searchAccessRequest(
            Pageable pageable,
            String visitorName, String companyName,
            String visitLocation, LocalDate visitDateFrom, LocalDate visitDateTo) throws AuthorityNotFoundException {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // determine company id restriction
        Integer companyIdRestriction = sdUserService.getCompanyIdRestriction();

        // determine user id restriction
        Integer userIdRestriction = sdUserService.getUserIdRestriction();

        LOG.info( String.format(
                "Searching visitor with {companyId: %s, userId: %s, " +
                        "visitorName: %s, companyName: %s, visitLocation: %s, visitDateFrom: %s, visitDateTo: %s}",
                companyIdRestriction, userIdRestriction,
                visitorName, companyName, visitLocation, visitDateFrom, visitDateTo) );

        // get request total count
        Integer totalCount = sdAccessRequestVisitorMapper.countSearchVisitor(
                companyIdRestriction, userIdRestriction,
                visitorName, companyName, visitLocation, visitDateFrom, visitDateTo);
        // get request content
        List<SdAccessRequestVisitorEntity> entityList = sdAccessRequestVisitorMapper.searchVisitor(
                offset, pageSize, companyIdRestriction, userIdRestriction,
                visitorName, companyName, visitLocation, visitDateFrom, visitDateTo);
        if( CollectionUtils.isEmpty(entityList) ){
            return new PageImpl<>(new LinkedList<>(), pageable, totalCount);
        }

        List<SdAccessRequestVisitorBean> beanList = new LinkedList<>();
        for (SdAccessRequestVisitorEntity entity : entityList){
            SdAccessRequestVisitorBean bean = new SdAccessRequestVisitorBean();
            sdAccessRequestVisitorBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return new PageImpl<>(beanList, pageable, totalCount);
    }

    @Override
    @Transactional
    public void createVisitorForAccessRequest(List<SdAccessRequestVisitorBean> visitorBeanList, Integer newAccessRequestId, SdUserBean requesterUserBean) {
        // prepare entity
        List<SdAccessRequestVisitorEntity> visitorEntityList = new LinkedList<>();
        removeEmptyVisitors(visitorBeanList);

        if(CollectionUtils.isEmpty(visitorBeanList)){
            throw new InvalidInputException("Missing visitor input.");
        }else {
            for(SdAccessRequestVisitorBean visitorBean : visitorBeanList){
                checkValidVisitor(visitorBean);

                SdAccessRequestVisitorEntity visitorEntity = new SdAccessRequestVisitorEntity();
                sdAccessRequestVisitorEntityPopulator.populate(visitorBean, visitorEntity);
                sdAccessRequestVisitorEntityPopulator.populate(requesterUserBean, visitorEntity);
                visitorEntity.setAccessRequestId(newAccessRequestId);
                visitorEntityList.add(visitorEntity);
            }
        }

        // insert visitor
        if(!CollectionUtils.isEmpty(visitorEntityList)){
            sdAccessRequestVisitorMapper.insertVisitors(visitorEntityList);
        }
    }

    @Override
    @Transactional
    public void checkin(Integer visitorAccessId, String visitorCardNum)
            throws AuthorityNotFoundException, AccessRequestVisitorNotFoundException, InvalidWorkflowException, AccessRequestNotFoundException {
        // check authority
        if( ! sdUserService.isInternalUser() ){
            throw new AuthorityNotFoundException("Authority not found for checking in visitor.");
        }

        // check request
        SdAccessRequestBean sdAccessRequestBean = sdAccessRequestService.getAccessRequestByVisitorId(visitorAccessId);
        sdAccessRequestService.checkRequestNowEligibleForCheckInOut(sdAccessRequestBean);

        // update
        int updateCount = sdAccessRequestVisitorMapper.checkin(visitorAccessId, visitorCardNum);

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
        if( ! sdUserService.isInternalUser() ){
            throw new AuthorityNotFoundException("Authority not found for checking out visitor.");
        }

        // check request
        SdAccessRequestBean sdAccessRequestBean = sdAccessRequestService.getAccessRequestByVisitorId(visitorAccessId);
        sdAccessRequestService.checkRequestNowEligibleForCheckInOut(sdAccessRequestBean);

        // update
        int updateCount = sdAccessRequestVisitorMapper.checkout(visitorAccessId);

        // check update
        if(updateCount==0){
            throw new AccessRequestVisitorNotFoundException("Cannot check-out visitorAccessId " + visitorAccessId + ".");
        }
    }

    private void removeEmptyVisitors(List<SdAccessRequestVisitorBean> requestVisitorBeanList){
        requestVisitorBeanList.removeIf(
                visitorBean -> visitorBean == null || (
                                StringUtils.isEmpty(visitorBean.getName()) &&
                                StringUtils.isEmpty(visitorBean.getCompanyName()) &&
                                StringUtils.isEmpty(visitorBean.getStaffId()) &&
                                StringUtils.isEmpty(visitorBean.getMobile())
                )
        );
    }

    private void checkValidVisitor(SdAccessRequestVisitorBean visitorBean) throws InvalidInputException {
        if(StringUtils.isEmpty(visitorBean.getName())){
            throw new InvalidInputException("Missing visitor's name.");
        }

        try {
            sdInputCheckService.checkName(visitorBean.getName());
            sdInputCheckService.checkCompanyName(visitorBean.getCompanyName());
            sdInputCheckService.checkStaffIdHkidPassport(visitorBean.getStaffId());
            sdInputCheckService.checkMobile(visitorBean.getMobile());
        } catch (InvalidInputException e){
            throw new InvalidInputException("Visitor " + visitorBean.getName() + ": " + e.getMessage());
        }
    }
}
