package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.SdInputCheckService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdCreateResultBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;
import com.hkt.btu.sd.facade.SdUserFacade;
import com.hkt.btu.sd.facade.data.*;
import com.hkt.btu.sd.facade.populator.SdUserDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SdUserFacadeImpl implements SdUserFacade {
    private static final Logger LOG = LogManager.getLogger(SdUserFacadeImpl.class);


    @Resource(name = "userService")
    SdUserService sdUserService;

    @Resource(name = "inputCheckService")
    SdInputCheckService sdInputCheckService;

    @Resource(name = "userDataPopulator")
    SdUserDataPopulator userDataPopulator;


    /**
     * Create PCCW / HKT user
     * userId Prefix T
     *
     * @param createUserFormData
     * @return userId
     */
    @Override
    public CreateResultData createUser(CreateUserFormData createUserFormData) {
        if (createUserFormData == null) {
            LOG.warn("Null sdUserData.");
            return null;
        }

        // Prepare User Data
        String name = StringUtils.trim(createUserFormData.getName());
        String email = StringUtils.trim(createUserFormData.getEmail());
        String mobile = StringUtils.trim(createUserFormData.getMobile());
        String employeeNumber = StringUtils.trim(createUserFormData.getUserId());

        List<String> userRoleIdList = createUserFormData.getUserRoleIdList();

        SdCreateResultBean resultBean;
        try {
            // check input
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkMobile(mobile);
            sdInputCheckService.checkEmployeeNumber(employeeNumber);
            // User maybe not have email,If have email check it.
            if (StringUtils.isNotEmpty(email)) {
                sdInputCheckService.checkEmail(email);
            }
            // PCCW / HKT user will use T prefix
            String userId = SdUserBean.CREATE_USER_PREFIX.PCCW_HKT_USER + employeeNumber;
            // create new user
            resultBean = sdUserService.createUser(userId, name, mobile, email, userRoleIdList);
        } catch (InvalidInputException | UserNotFoundException | DuplicateUserEmailException e) {
            LOG.warn(e.getMessage());
            return CreateResultData.of(e.getMessage());
        }
        return new CreateResultData(resultBean.getUserId(), null, resultBean.getPassword());
    }

    /**
     * Create Non PCCW / HKT user
     * userId prefix X
     *
     * @param createUserFormData
     * @return userId
     */
    @Override
    public CreateResultData createNonPccwHktUser(CreateUserFormData createUserFormData) {
        if (createUserFormData == null) {
            LOG.warn("Null sdUserData.");
            return null;
        }

        // Prepare User Data
        String name = StringUtils.trim(createUserFormData.getName());
        String email = StringUtils.trim(createUserFormData.getEmail());
        String mobile = StringUtils.trim(createUserFormData.getMobile());
        String ldapDomain = StringUtils.trim(createUserFormData.getLdapDomain());
        String employeeNumber = StringUtils.trim(createUserFormData.getUserId());
        List<String> userRoleIdList = createUserFormData.getUserRoleIdList();

        String newUserId;

        SdCreateResultBean resultBean;
        try {
            // check input
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkMobile(mobile);
            sdInputCheckService.checkEmployeeNumber(employeeNumber);
            // User maybe not have email,If have email check it.
            if (StringUtils.isNotEmpty(email)) {
                sdInputCheckService.checkEmail(email);
            }
            // if user not have userId, wiil use X prefix.
            // create new user.
            resultBean = sdUserService.createUser(null, name, mobile, email, userRoleIdList);
        } catch (InvalidInputException | UserNotFoundException | DuplicateUserEmailException e) {
            LOG.warn(e.getMessage());
            return CreateResultData.of(e.getMessage());
        }

        return new CreateResultData(resultBean.getUserId(), null, resultBean.getPassword());
    }

    /**
     * Create LDAP user
     *
     * @param createUserFormData
     * @return userId
     */
    @Override
    public CreateResultData createLdapUser(CreateUserFormData createUserFormData) {
        if (createUserFormData == null) {
            LOG.warn("Null sdUserData.");
            return null;
        }

        // Prepare User Data
        String name = StringUtils.trim(createUserFormData.getName());
        String mobile = StringUtils.trim(createUserFormData.getMobile());
        String ldapDomain = StringUtils.trim(createUserFormData.getLdapDomain());
        String employeeNumber = StringUtils.trim(createUserFormData.getUserId());
        List<String> userRoleIdList = createUserFormData.getUserRoleIdList();

        String newUserId;
        try {
            // check input
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkMobile(mobile);
            sdInputCheckService.checkEmployeeNumber(employeeNumber);
            // create new LDAP user.
            newUserId = sdUserService.createLdapUser(name, mobile, employeeNumber, ldapDomain, userRoleIdList);
        } catch (DuplicateUserEmailException | UserNotFoundException e) {
            LOG.warn(e.getMessage());
            return CreateResultData.of(e.getMessage());
        }

        return new CreateResultData(newUserId, null);
    }

    @Override
    public String updateUser(UpdateUserFormData updateUserFormData) {
        if (updateUserFormData == null) {
            return "Null input.";
        } else if (updateUserFormData.getUserId() == null) {
            return "Empty user ID.";
        } else if (CollectionUtils.isEmpty(updateUserFormData.getUserRoleIdList())) {
            return "Empty authority";
        }

        String userId = updateUserFormData.getUserId();
        String name = StringUtils.trim(updateUserFormData.getName());
        String mobile = StringUtils.trim(updateUserFormData.getMobile());
        String staffId = StringUtils.trim(updateUserFormData.getStaffId());
        List<String> userRoleIdList = updateUserFormData.getUserRoleIdList();

        Boolean isAdmin = updateUserFormData.isUserGroupAdmin();
        Boolean isUser = updateUserFormData.isUserGroupUser();
        Boolean isCAdmin = updateUserFormData.isUserGroupCAdmin();
        Boolean isCUser = updateUserFormData.isUserGroupCUser();

        // check input
        try {
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkMobile(mobile);
        } catch (InvalidInputException e) {
            return e.getMessage();
        }

        try {
            sdUserService.updateUser(userId, name, mobile, userRoleIdList);
        } catch (UserNotFoundException | InsufficientAuthorityException | InvalidInputException e) {
            LOG.warn(e.getMessage());
            return e.getMessage();
        } catch (GeneralSecurityException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }


    public String activateUser(String userId) {
        try {
            // check access to user
            SdUserBean userBean = sdUserService.getUserByUserId(userId);
            String username = userBean.getEmail();

            // activate user
            sdUserService.activateUserByUsername(username);
            return null;
        } catch (UserNotFoundException | InvalidInputException e) {
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String deactivateUser(String userId) {
        try {
            // check access to user
            SdUserBean userBean = sdUserService.getUserByUserId(userId);
            String username = userBean.getEmail();

            // deactivate user
            sdUserService.disableUserByUsername(username);
            return null;
        } catch (UserNotFoundException | InvalidInputException e) {
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String updateCurrentUserPwd(UpdatePwdFormData updatePwdFormData) {
        SdUserData sdUserData = getCurrentUser();
        String oldPassword = updatePwdFormData.getOldPassword();
        String newPassword = updatePwdFormData.getNewPassword();
        String newPasswordRe = updatePwdFormData.getNewPasswordRe();

        if (sdUserData == null) {
            return "Current user not found.";
        } else if (StringUtils.isEmpty(oldPassword)) {
            return "Empty old password.";
        } else if (StringUtils.isEmpty(newPassword)) {
            return "Empty new password.";
        } else if (StringUtils.equals(oldPassword, newPasswordRe)) {
            return "Old and new password cannot be the same.";
        } else if (!StringUtils.equals(newPassword, newPasswordRe)) {
            return "Re-enter new password not match.";
        }

        // update password
        try {
            sdUserService.updateUserPwd(sdUserData.getUserId(), oldPassword, newPassword);
        } catch (UserNotFoundException e) {
            LOG.warn("Data rollback.");
            LOG.warn(e.getMessage(), e);
            return e.getMessage();
        } catch (InvalidPasswordException e) {
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
        if (StringUtils.isEmpty(otp)) {
            return "Empty reset otp.";
        } else if (StringUtils.isEmpty(newPassword)) {
            return "Empty new password.";
        } else if (!StringUtils.equals(newPassword, newPasswordRe)) {
            return "Re-enter new password not match.";
        }

        // reset password
        try {
            sdUserService.resetPwd(otp, newPassword);
        } catch (UserNotFoundException e) {
            LOG.warn("Data rollback.");
            LOG.warn(e.getMessage(), e);
            return e.getMessage();
        } catch (InvalidPasswordException | InvalidOtpException e) {
            LOG.warn("Data rollback.");
            LOG.warn(e.getMessage());
            return e.getMessage();
        }

        return null;
    }

    @Override
    public String requestResetPassword(String email) {
        if (StringUtils.isEmpty(email)) {
            return "Empty email input.";
        }

        try {
            sdUserService.requestResetPassword(email);
        } catch (UserNotFoundException e) {
            LOG.warn("User not found (" + email + ").");
            return e.getMessage();
        } catch (MessagingException e) {
            LOG.error(e.getMessage(), e);
            return "Failed to send email.";
        } catch (InvalidUserTypeException | InvalidOtpException e) {
            return e.getMessage();
        }

        return null;
    }

    @Override
    public PageData<SdUserData> searchUser(Pageable pageable, String userId, String email, String name) {
        email = StringUtils.trimToNull(email);
        name = StringUtils.trimToNull(name);
        userId = StringUtils.trimToNull(userId);

        Page<SdUserBean> pageBean;
        try {
            pageBean = sdUserService.searchUser(pageable, userId, email, name);
        } catch (AuthorityNotFoundException e) {
            return new PageData<>(e.getMessage());
        }

        // populate content
        List<SdUserBean> beanList = pageBean.getContent();
        List<SdUserData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdUserBean bean : beanList) {
                SdUserData data = new SdUserData();
                userDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public SdUserData getCurrentUser() {
        SdUserData userData = new SdUserData();

        try {
            // get user info
            SdUserBean sdUserBean = (SdUserBean) sdUserService.getCurrentUserBean();
            userDataPopulator.populate(sdUserBean, userData);
            // TODO: Encryption function will be done later
            userDataPopulator.populateSensitiveData(sdUserBean, userData);

        } catch (UserNotFoundException e) {
            return null;
        }

        return userData;
    }

    public SdUserData getUserByUserId(String userId) {
        SdUserData userData = new SdUserData();

        try {
            // get user info
            SdUserBean sdUserBean = sdUserService.getUserByUserId(userId);
            userDataPopulator.populate(sdUserBean, userData);
            userDataPopulator.populateSensitiveData(sdUserBean, userData);

        } catch (UserNotFoundException e) {
            LOG.warn(e.getMessage());
            return null;
        }

        return userData;
    }

    @Override
    public boolean isInternalUser() {
        return sdUserService.isInternalUser();
    }
}
