package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.SdInputCheckService;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdCreateResultBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
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

    @Resource(name = "userRoleService")
    SdUserRoleService userRoleService;


    /**
     * Create PCCW / HKT user
     * userId Prefix T
     *
     * @return userId
     */
    @Override
    public SdCreateResultData createPccwHktUser(SdCreateUserFormData createUserFormData) {
        if (createUserFormData == null) {
            LOG.warn("Null sdUserData.");
            return null;
        }

        // Prepare User Data
        String name = StringUtils.trim(createUserFormData.getName());
        String email = StringUtils.trim(createUserFormData.getEmail());
        String mobile = StringUtils.trim(createUserFormData.getMobile());
        String userId = StringUtils.trim(createUserFormData.getUserId());
        String employeeNumber = StringUtils.trim(createUserFormData.getStaffId());

        List<String> userRoleIdList = createUserFormData.getUserRoleIdList();

        SdCreateResultBean resultBean;
        try {
            // check input
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkEmail(email);
            sdInputCheckService.checkMobile(mobile);
            sdInputCheckService.checkPccwHktLoginID(userId);
            sdInputCheckService.checkEmployeeNumber(employeeNumber);
            String primaryRoleId = checkPrimaryRole(createUserFormData.getPrimaryRoleId(), userRoleIdList);
            // create new user
            resultBean = sdUserService.createUser(userId, employeeNumber, name, mobile, email, primaryRoleId, userRoleIdList);
        } catch (InvalidInputException | UserNotFoundException | DuplicateUserEmailException e) {
            LOG.warn(e.getMessage());
            return SdCreateResultData.of(e.getMessage());
        }
        return new SdCreateResultData(resultBean.getUserId(), null, resultBean.getPassword(), null);
    }

    /**
     * Create Non PCCW / HKT user
     * userId prefix X
     *
     * @return userId
     */
    @Override
    public SdCreateResultData createNonPccwHktUser(SdCreateUserFormData createUserFormData) {
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

        String newUserId;

        SdCreateResultBean resultBean;
        try {
            // check input
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkEmail(email);
            sdInputCheckService.checkMobile(mobile);
            sdInputCheckService.checkNonPccwHktLoginId(employeeNumber);
            sdInputCheckService.checkAssignRoleByDomain(userRoleIdList, null);
            String primaryRoleId = checkPrimaryRole(createUserFormData.getPrimaryRoleId(),userRoleIdList);
            // create new user.
            // Non PCCW / HKT user will use X prefix
            resultBean = sdUserService.createUser(employeeNumber, null, name, mobile, email,primaryRoleId, userRoleIdList);
        } catch (InvalidInputException | UserNotFoundException | DuplicateUserEmailException e) {
            LOG.warn(e.getMessage());
            return SdCreateResultData.of(e.getMessage());
        }

        return new SdCreateResultData(resultBean.getUserId(), null, resultBean.getPassword(), null);
    }

    private String checkPrimaryRole(String primaryRoleId, List<String> userRoleIdList) {
        if (StringUtils.isBlank(primaryRoleId)) {
            throw new InvalidInputException("Empty input primary role");
        }
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            throw new InvalidInputException("Please select at least one user role");
        }
        if (userRoleIdList.contains(primaryRoleId)) {
            List<String> primaryRoles = userRoleService.getPrimaryRoles();
            if (!primaryRoles.contains(primaryRoleId)) {
                throw new InvalidInputException("Primary role not match user role.");
            }
        } else {
            throw new InvalidInputException("Primary role not match user role.");
        }
        return primaryRoleId;
    }

    /**
     * Create LDAP user
     *
     * @return userId
     */
    @Override
    public SdCreateResultData createLdapUser(SdCreateUserFormData createUserFormData) {
        if (createUserFormData == null) {
            LOG.warn("Null sdUserData.");
            return null;
        }

        String employeeNumber = StringUtils.trim(createUserFormData.getUserId());

        // Prepare User Data
        String name = StringUtils.trim(createUserFormData.getName());
        String email = StringUtils.trim(createUserFormData.getEmail());
        String mobile = StringUtils.trim(createUserFormData.getMobile());
        String ldapDomain = StringUtils.trim(createUserFormData.getLdapDomain());
        List<String> userRoleIdList = createUserFormData.getUserRoleIdList();

        String newUserId;
        try {
            // check input
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkEmail(email);
            sdInputCheckService.checkMobile(mobile);
            sdInputCheckService.checkEmployeeNumber(employeeNumber);
            String primaryRoleId = checkPrimaryRole(createUserFormData.getPrimaryRoleId(), userRoleIdList);
            // create new LDAP user.
            newUserId = sdUserService.createLdapUser(name, mobile, employeeNumber, ldapDomain, email, primaryRoleId, userRoleIdList);
        } catch (DuplicateUserEmailException | UserNotFoundException | InvalidInputException e) {
            LOG.warn(e.getMessage());
            return SdCreateResultData.of(e.getMessage());
        }

        return new SdCreateResultData(newUserId, null);
    }

    @Override
    public String updateUser(SdUpdateUserFormData updateUserFormData) {
        if (updateUserFormData == null) {
            return "Null input.";
        } else if (updateUserFormData.getUserId() == null) {
            return "Empty user ID.";
        } else if (CollectionUtils.isEmpty(updateUserFormData.getUserRoleIdList())) {
            return "Empty authority";
        } else if (StringUtils.isBlank(updateUserFormData.getPrimaryRoleId())) {
            return "Empty Primary Role";
        }

        String userId = updateUserFormData.getUserId();
        String name = StringUtils.trim(updateUserFormData.getName());
        String email = StringUtils.trim(updateUserFormData.getEmail());
        String mobile = StringUtils.trim(updateUserFormData.getMobile());
        List<String> userRoleIdList = updateUserFormData.getUserRoleIdList();

        SdUserBean userBean = sdUserService.getUserByUserId(userId);
        if (userBean == null) {
            return "User not found.";
        }
        String ldapDomain = userBean.getLdapDomain();
        // check input
        try {
            sdInputCheckService.checkName(name);
            if (StringUtils.isNotEmpty(email)) {
                sdInputCheckService.checkEmail(email);
            }
            sdInputCheckService.checkMobile(mobile);
//            sdInputCheckService.checkAssignRoleByDomain(userRoleIdList, ldapDomain);
//            sdInputCheckService.checkUserRole(userRoleIdList, userRoleService.getEligibleUserRoleGrantList(), userRoleService.getUserRoleByUserId(userId, true));
        } catch (InvalidInputException e) {
            return e.getMessage();
        }

        try {
            sdUserService.updateUser(userId, name, mobile, email, updateUserFormData.getPrimaryRoleId(), userRoleIdList);
        } catch (UserNotFoundException | InsufficientAuthorityException | InvalidInputException e) {
            LOG.warn(e.getMessage());
            return e.getMessage();
        } catch (GeneralSecurityException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        }

        return null;
    }

    @Override
    public SdChangeUserTypeResultData changeUserTypeToPccwOrHktUser(SdChangeUserTypeFormData changeUserTypeFormData) {
        if (changeUserTypeFormData == null) {
            return SdChangeUserTypeResultData.ofMsg("Null input.");
        }
        // Check UserId
        if (StringUtils.isEmpty(changeUserTypeFormData.getUserId()) || StringUtils.isEmpty(changeUserTypeFormData.getNewUserId())) {
            return SdChangeUserTypeResultData.ofMsg("Empty User Id");
        }
        String oldUserId = changeUserTypeFormData.getUserId();
        String name = StringUtils.trim(changeUserTypeFormData.getName());
        String email = StringUtils.trim(changeUserTypeFormData.getEmail());
        String mobile = StringUtils.trim(changeUserTypeFormData.getMobile());
        String employeeNumber = StringUtils.trim(changeUserTypeFormData.getNewUserId());
        String userId;
        try {
            // check input
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkMobile(mobile);
            sdInputCheckService.checkPccwHktLoginID(employeeNumber);
            sdInputCheckService.checkEmail(email);
            // change user type
            userId = sdUserService.changeUserTypeToPCCWOrHktUser(oldUserId, name, mobile, employeeNumber, email);
            return SdChangeUserTypeResultData.ofUser(userId);
        } catch (InvalidInputException | UserNotFoundException e) {
            LOG.warn(e.getMessage());
            return SdChangeUserTypeResultData.ofMsg(e.getMessage());
        }
    }

    @Override
    public SdChangeUserTypeResultData changeUserTypeToNonPccwOrHktUser(SdChangeUserTypeFormData changeUserTypeFormData) {
        if (changeUserTypeFormData == null) {
            return SdChangeUserTypeResultData.ofMsg("Null input.");
        }
        // Check UserId
        if (StringUtils.isEmpty(changeUserTypeFormData.getUserId()) || StringUtils.isEmpty(changeUserTypeFormData.getNewUserId())) {
            return SdChangeUserTypeResultData.ofMsg("Empty User Id");
        }
        String oldUserId = changeUserTypeFormData.getUserId();
        String name = StringUtils.trim(changeUserTypeFormData.getName());
        String email = StringUtils.trim(changeUserTypeFormData.getEmail());
        String mobile = StringUtils.trim(changeUserTypeFormData.getMobile());
        String employeeNumber = StringUtils.trim(changeUserTypeFormData.getNewUserId());
        String userId;
        try {
            // check input
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkMobile(mobile);
            sdInputCheckService.checkNonPccwHktLoginId(employeeNumber);
            if (StringUtils.isNotEmpty(email)) {
                sdInputCheckService.checkEmail(email);
            }
            // change user type
            userId = sdUserService.changeUserTypeToNonPCCWOrHktUser(oldUserId, name, mobile, employeeNumber, email);
            return SdChangeUserTypeResultData.ofUser(userId);
        } catch (InvalidInputException | UserNotFoundException e) {
            LOG.warn(e.getMessage());
            return SdChangeUserTypeResultData.ofMsg(e.getMessage());
        }
    }

    @Override
    public SdChangeUserTypeResultData changeUserTypeToLdapUser(SdChangeUserTypeFormData changeUserTypeFormData) {
        if (changeUserTypeFormData == null) {
            return SdChangeUserTypeResultData.ofMsg("Null input.");
        }

        if (StringUtils.isEmpty(changeUserTypeFormData.getUserId()) || StringUtils.isEmpty(changeUserTypeFormData.getNewUserId())) {
            return SdChangeUserTypeResultData.ofMsg("Empty User Id");
        }
        String oldUserId = changeUserTypeFormData.getUserId();
        String name = StringUtils.trim(changeUserTypeFormData.getName());
        String email = StringUtils.trim(changeUserTypeFormData.getEmail());
        String ldapDomain = StringUtils.trim(changeUserTypeFormData.getLdapDomain());
        String mobile = StringUtils.trim(changeUserTypeFormData.getMobile());
        String employeeNumber = StringUtils.trim(changeUserTypeFormData.getNewUserId());
        String userId;
        try {
            // check input
            sdInputCheckService.checkName(name);
            sdInputCheckService.checkEmail(email);
            sdInputCheckService.checkMobile(mobile);
            sdInputCheckService.checkLdapDomain(ldapDomain);
            sdInputCheckService.checkEmployeeNumber(employeeNumber);
            // oldUserId, name , mobile, employeeNumber , ldapDomain.
            userId = sdUserService.changeUserTypeToLdapUser(oldUserId, name, mobile, employeeNumber, ldapDomain, email);
            return SdChangeUserTypeResultData.ofUser(userId);
        } catch (InvalidInputException | UserNotFoundException e) {
            LOG.warn(e.getMessage());
            return SdChangeUserTypeResultData.ofMsg(e.getMessage());
        }
    }


    public String activateUser(String userId) {
        try {
            // check access to user
            SdUserBean userBean = sdUserService.getUserByUserId(userId);
            String username = userBean.getUserId();

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
            String username = userBean.getUserId();

            // deactivate user
            sdUserService.disableUserByUsername(username);
            return null;
        } catch (UserNotFoundException | InvalidInputException e) {
            LOG.warn(e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    public String updateCurrentUserPwd(SdUpdatePwdFormData updatePwdFormData) {
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
    public String resetPassword(SdResetPwdFormData resetPwdFormData) {
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
    public void resetPwd4NonLdapUser(String userId) {
        Optional.ofNullable(userId).filter(StringUtils::isNotBlank).ifPresentOrElse(s -> {
            try {
                sdUserService.requestResetPassword(userId);
            } catch (MessagingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }, () -> {
            throw new RuntimeException("Reset password error: User id is empty.");
        });
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

        } catch (UserNotFoundException | InsufficientAuthorityException e) {
            LOG.warn(e.getMessage());
            return null;
        }

        return userData;
    }

    @Override
    public PageData<SdUserData> getTeamHeadUser(Pageable pageable, String teamHead) {
        Page<SdUserBean> pageBean;
        try {
            pageBean = sdUserService.getTeamHeadUser(pageable, teamHead);
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
    public List<SdUserData> getUserByRoleId(String roleId) {
        // populate content
        List<SdUserBean> beanList = sdUserService.getUserByRoleId(roleId);
        List<SdUserData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdUserBean bean : beanList) {
                SdUserData data = new SdUserData();
                userDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }
        return dataList;
    }
}
