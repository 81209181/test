package com.hkt.btu.noc.facade.impl;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.noc.core.exception.*;
import com.hkt.btu.noc.core.service.NocCompanyService;
import com.hkt.btu.noc.core.service.NocInputCheckService;
import com.hkt.btu.noc.core.service.NocUserService;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import com.hkt.btu.noc.facade.NocUserFacade;
import com.hkt.btu.noc.facade.data.*;
import com.hkt.btu.noc.facade.populator.NocCompanyDataPopulator;
import com.hkt.btu.noc.facade.populator.NocUserDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class NocUserFacadeImpl implements NocUserFacade {
    private static final Logger LOG = LogManager.getLogger(NocUserFacadeImpl.class);


    @Resource(name = "userService")
    NocUserService nocUserService;
    @Resource(name = "companyService")
    NocCompanyService nocCompanyService;
    @Resource(name = "inputCheckService")
    NocInputCheckService nocInputCheckService;

    @Resource(name = "userDataPopulator")
    NocUserDataPopulator userDataPopulator;
    @Resource(name = "companyDataPopulator")
    NocCompanyDataPopulator nocCompanyDataPopulator;


    @Override
    public CreateResultData createUser(CreateUserFormData createUserFormData) {
        if(createUserFormData==null){
            LOG.warn("Null nocUserData.");
            return null;
        }

        String name = StringUtils.trim( createUserFormData.getName() );
        String mobile = StringUtils.trim( createUserFormData.getMobile() );
        String staffId = StringUtils.trim(  createUserFormData.getStaffId() );
        String email = StringUtils.trim(  createUserFormData.getEmail() );
        Integer companyId = createUserFormData.getCompanyId();
        List<String> userGroupIdList = createUserFormData.getUserGroupIdList();

        // check input
        try {
            nocInputCheckService.checkName(name);
            nocInputCheckService.checkMobile(mobile);
            nocInputCheckService.checkStaffIdHkidPassport(staffId);
            nocInputCheckService.checkEmail(email);
        }catch (InvalidInputException e){
            return CreateResultData.of(e.getMessage());
        }

        // create new user
        Integer newUserId;
        try {
            newUserId = nocUserService.createUser(name, mobile, email, staffId, companyId, userGroupIdList);
        } catch (InvalidInputException | UserNotFoundException | DuplicateUserEmailException | GeneralSecurityException e){
            LOG.warn(e.getMessage());
            return CreateResultData.of(e.getMessage());
        }

        return CreateResultData.of(newUserId);
    }

    @Override
    public LinkedList<NocCompanyData> getEligibleCompanyList() {
        List<NocCompanyBean> nocCompanyBeanList = nocUserService.getEligibleCompanyList();
        if(CollectionUtils.isEmpty(nocCompanyBeanList)){
            return new LinkedList<>();
        }

        LinkedList<NocCompanyData> dataList = new LinkedList<>();
        for(NocCompanyBean bean : nocCompanyBeanList){
            NocCompanyData data = new NocCompanyData();
            nocCompanyDataPopulator.populate(bean, data);
            dataList.add(data);
        }

        dataList.sort(Comparator.comparing(NocCompanyData::getName));
        return dataList;
    }

    @Override
    public String updateUser(UpdateUserFormData updateUserFormData) {
        if(updateUserFormData==null){
            return "Null input.";
        } else if(updateUserFormData.getUserId()==null){
            return "Empty user ID.";
        }

        Integer userId = updateUserFormData.getUserId();
        String name = StringUtils.trim( updateUserFormData.getName() );
        String mobile = StringUtils.trim( updateUserFormData.getMobile() );
        String staffId = StringUtils.trim( updateUserFormData.getStaffId() );

        Boolean isAdmin = updateUserFormData.isUserGroupAdmin();
        Boolean isUser = updateUserFormData.isUserGroupUser();
        Boolean isCAdmin = updateUserFormData.isUserGroupCAdmin();
        Boolean isCUser = updateUserFormData.isUserGroupCUser();

        // check input
        try {
            nocInputCheckService.checkName(name);
            nocInputCheckService.checkMobile(mobile);
            nocInputCheckService.checkStaffIdHkidPassport(staffId);
        } catch (InvalidInputException e){
            return e.getMessage();
        }

        try {
            nocUserService.updateUser(userId, name, mobile, staffId, isAdmin, isUser, isCAdmin, isCUser);
        } catch (UserNotFoundException | InsufficientAuthorityException | InvalidInputException e) {
            LOG.warn(e.getMessage());
            return e.getMessage();
        } catch (GeneralSecurityException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }


    public String activateUser(Integer userId) {
        try {
            // check access to user
            NocUserBean userBean = nocUserService.getUserByUserId(userId);
            String username = userBean.getEmail();

            // activate user
            nocUserService.activateUserByUsername(username);
            return null;
        } catch (UserNotFoundException | InvalidInputException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String deactivateUser(Integer userId) {
        try {
            // check access to user
            NocUserBean userBean = nocUserService.getUserByUserId(userId);
            String username = userBean.getEmail();

            // deactivate user
            nocUserService.disableUserByUsername(username);
            return null;
        } catch (UserNotFoundException | InvalidInputException e){
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String updateCurrentUserPwd(UpdatePwdFormData updatePwdFormData) {
        NocUserData nocUserData = getCurrentUser();
        String oldPassword = updatePwdFormData.getOldPassword();
        String newPassword = updatePwdFormData.getNewPassword();
        String newPasswordRe = updatePwdFormData.getNewPasswordRe();

        if(nocUserData==null){
            return "Current user not found.";
        } else if(StringUtils.isEmpty(oldPassword)){
            return "Empty old password.";
        } else if(StringUtils.isEmpty(newPassword)){
            return "Empty new password.";
        } else if(StringUtils.equals(oldPassword, newPasswordRe)){
            return "Old and new password cannot be the same.";
        } else if(! StringUtils.equals(newPassword, newPasswordRe)){
            return "Re-enter new password not match.";
        }

        // update password
        try {
            nocUserService.updateUserPwd(nocUserData.getUserId(), oldPassword, newPassword);
        } catch (UserNotFoundException e){
            LOG.warn("Data rollback.");
            LOG.warn(e.getMessage(), e);
            return e.getMessage();
        } catch (InvalidPasswordException e){
            LOG.warn("Data rollback.");
            LOG.warn(e.getMessage());
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String resetPassword(ResetPwdFormData resetPwdFormData) {
        String otp = resetPwdFormData.getResetOtp();
        String newPassword = resetPwdFormData.getNewPassword();
        String newPasswordRe = resetPwdFormData.getNewPasswordRe();
        if(StringUtils.isEmpty(otp)){
            return "Empty reset otp.";
        } else if(StringUtils.isEmpty(newPassword)){
            return "Empty new password.";
        } else if(! StringUtils.equals(newPassword, newPasswordRe)){
            return "Re-enter new password not match.";
        }

        // reset password
        try {
            nocUserService.resetPwd(otp, newPassword);
        } catch (UserNotFoundException e){
            LOG.warn("Data rollback.");
            LOG.warn(e.getMessage(), e);
            return e.getMessage();
        } catch (InvalidPasswordException | InvalidInputException e){
            LOG.warn("Data rollback.");
            LOG.warn(e.getMessage());
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String requestResetPassword(String email) {
        if( StringUtils.isEmpty(email) ){
            return "Empty email input.";
        }

        try {
            nocUserService.requestResetPassword(email);
        } catch (UserNotFoundException e){
            LOG.warn("User not found (" + email + ").");
            return e.getMessage();
        } catch (MessagingException e){
            LOG.error(e.getMessage(), e);
            return "Failed to send email.";
        }

        return null;
    }

    @Override
    public PageData<NocUserData> searchUser(Pageable pageable, Integer userId, String email, String name, String userGroupId) {
        email = StringUtils.trimToNull(email);
        name = StringUtils.trimToNull(name);
        userGroupId = StringUtils.trimToNull(userGroupId);

        Page<NocUserBean> pageBean;
        try {
            pageBean = nocUserService.searchUser(pageable, userId, email, name, userGroupId);
        }catch (AuthorityNotFoundException e){
            return new PageData<>(e.getMessage());
        }

        // populate content
        List<NocUserBean> beanList = pageBean.getContent();
        List<NocUserData> dataList = new LinkedList<>();
        if(!CollectionUtils.isEmpty(beanList)){
            for(NocUserBean bean : beanList){
                NocUserData data = new NocUserData();
                userDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public NocUserData getCurrentUser() {
        NocUserData userData = new NocUserData();

        try {
            // get user info
            NocUserBean nocUserBean = (NocUserBean) nocUserService.getCurrentUserBean();
            userDataPopulator.populate(nocUserBean, userData);
            userDataPopulator.populateSensitiveData(nocUserBean, userData);

            // get company info
            NocCompanyBean nocCompanyBean = nocCompanyService.getCompanyById(nocUserBean.getCompanyId());
            userDataPopulator.populate(nocCompanyBean, userData);
        } catch (UserNotFoundException e){
            return null;
        } catch (CompanyNotFoundException e){
            LOG.warn("Company data corrupted of companyId=" + userData.getCompanyId() + ".");
            return userData;
        }

        return userData;
    }

    public NocUserData getUserByUserId(Integer userId){
        NocUserData userData = new NocUserData();

        try {
            // get user info
            NocUserBean nocUserBean = nocUserService.getUserByUserId(userId);
            userDataPopulator.populate(nocUserBean, userData);
            userDataPopulator.populateSensitiveData(nocUserBean, userData);

            // get company info
            NocCompanyBean nocCompanyBean = nocCompanyService.getCompanyById(nocUserBean.getCompanyId());
            userDataPopulator.populate(nocCompanyBean, userData);
        } catch (UserNotFoundException e){
            LOG.warn(e.getMessage());
            return null;
        } catch (CompanyNotFoundException e){
            LOG.warn("Company data corrupted of companyId=" + userData.getCompanyId() + ".");
            return userData;
        }

        return userData;
    }

    @Override
    public boolean isInternalUser() {
        return nocUserService.isInternalUser();
    }
}
