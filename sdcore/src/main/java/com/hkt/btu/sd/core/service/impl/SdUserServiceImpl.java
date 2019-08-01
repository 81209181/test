package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.impl.BtuUserServiceImpl;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserGroupMapper;
import com.hkt.btu.sd.core.dao.mapper.SdUserMapper;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.*;
import com.hkt.btu.sd.core.service.bean.SdEmailBean;
import com.hkt.btu.sd.core.service.bean.SdOtpBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import com.hkt.btu.sd.core.service.populator.SdUserBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.*;

public class SdUserServiceImpl extends BtuUserServiceImpl implements SdUserService {
    private static final Logger LOG = LogManager.getLogger(SdUserServiceImpl.class);

    @Resource
    SdUserMapper sdUserMapper;
    @Resource
    SdUserGroupMapper sdUserGroupMapper;

    @Resource(name = "userBeanPopulator")
    SdUserBeanPopulator sdUserBeanPopulator;

    @Resource(name = "userGroupService")
    SdUserGroupService sdUserGroupService;
    @Resource(name = "otpService")
    SdOtpService sdOtpService;
    @Resource(name = "emailService")
    SdEmailService sdEmailService;

    @Resource(name = "sensitiveDataService")
    SdSensitiveDataService sdSensitiveDataService;

    @Override
    public BtuUserBean getCurrentUserBean() throws UserNotFoundException {
        BtuUser btuUser = this.getCurrentUser();
        BtuUserBean userBean = btuUser.getUserBean();
        SdUserBean sdUserBean = new SdUserBean();
        sdUserBean.setUserId(Integer.parseInt(userBean.getUsername()));
        sdUserBean.setName(userBean.getUsername());
        btuUser.setUserBean(sdUserBean);
        if (btuUser == null) {
            throw new UserNotFoundException("No UserBean found in security context!");
        } else if (btuUser.getUserBean() instanceof SdUserBean) {
            return btuUser.getUserBean();
        }
        throw new UserNotFoundException("No SdUserBean found in security context!");
    }


    @Override
    public BtuUserBean getUserBeanByUsername(String username) {
        // make email lower case
        String email = StringUtils.lowerCase(username);

        // get user data
        SdUserEntity sdUserEntity = sdUserMapper.getUserByEmail(email);
        if (sdUserEntity == null) {
            return null;
        }

        // get user group data
        List<SdUserGroupEntity> groupEntityList = sdUserGroupMapper.getUserGroupByUserId(sdUserEntity.getUserId());

        // construct bean
        SdUserBean userBean = new SdUserBean();
        sdUserBeanPopulator.populate(sdUserEntity, userBean);
        sdUserBeanPopulator.populate(groupEntityList, userBean);

        return userBean;
    }

    @Override
    public Integer getCurrentUserUserId() {
        SdUserBean sdUserBean = (SdUserBean) getCurrentUserBean();
        return sdUserBean.getUserId();
    }

    @Override
    public SdUserBean getUserByUserId(Integer userId) throws UserNotFoundException {
        if (userId == null) {
            throw new UserNotFoundException("Empty user id input.");
        }

        // determine company id restriction
        Integer companyId = getCompanyIdRestriction();

        // get user data
        SdUserEntity sdUserEntity = sdUserMapper.getUserByUserId(userId, companyId);
        if (sdUserEntity == null) {
            throw new UserNotFoundException("Cannot find user with id " + userId + ".");
        }

        // get user group data
        List<SdUserGroupEntity> groupEntityList = sdUserGroupMapper.getUserGroupByUserId(userId);

        // construct bean
        SdUserBean userBean = new SdUserBean();
        sdUserBeanPopulator.populate(sdUserEntity, userBean);
        sdUserBeanPopulator.populate(groupEntityList, userBean);

        return userBean;
    }


    @Transactional
    public Integer createUser(String name, String mobile, String email, String staffId,
                              Integer companyId, List<String> groupIdList)
            throws DuplicateUserEmailException, UserNotFoundException, GeneralSecurityException {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidInputException("Empty user name.");
        } else if (StringUtils.isEmpty(email)) {
            throw new InvalidInputException("Empty email.");
        }

        // get current user user id
        Integer createby = getCurrentUserUserId();

        // check current user has right to create user of user group
        boolean isEligibleUserGroup = sdUserGroupService.isEligibleToGrantUserGroup(groupIdList);
        if (!isEligibleUserGroup) {
            LOG.warn("Ineligible to create user of selected user group (" + groupIdList + ") by user (" + createby + ").");
            throw new InvalidInputException("Invalid user group.");
        }


        // check email duplicated
        SdUserEntity userEntity = sdUserMapper.getUserByEmail(email);
        if (userEntity != null) {
            throw new DuplicateUserEmailException();
        }

        // make email lower case (**assume email are all lower case)
        email = StringUtils.lowerCase(email);

        // generate dummy password
        UUID uuid = UUID.randomUUID();
        String password = uuid.toString();
        String encodedPassword = encodePassword(password);

        // encrypt
        byte[] encryptedMobile = StringUtils.isEmpty(mobile) ? null : sdSensitiveDataService.encryptFromString(mobile);
        byte[] encryptedStaffId = StringUtils.isEmpty(staffId) ? null : sdSensitiveDataService.encryptFromString(staffId);

        // prepare entity
        SdUserEntity sdUserEntity = new SdUserEntity();
        sdUserEntity.setName(name);
        sdUserEntity.setStatus(SdUserEntity.STATUS.ACTIVE);
        sdUserEntity.setMobile(encryptedMobile);
        sdUserEntity.setEmail(email);
        sdUserEntity.setCompanyId(companyId);
        sdUserEntity.setStaffId(encryptedStaffId);
        sdUserEntity.setPassword(encodedPassword);
        sdUserEntity.setCreateby(createby);

        // create user in db
        sdUserMapper.insertUser(sdUserEntity);

        // get new user id
        Integer newUserId = sdUserEntity.getUserId();

        // create user group relation in db
        if (!CollectionUtils.isEmpty(groupIdList)) {
            for (String groupId : groupIdList) {
                sdUserGroupMapper.insertUserUserGroup(newUserId, groupId, createby);
            }
        }

        LOG.info("User (id: " + newUserId + ") " + email + " created.");

        // send init password email
        try {
            requestResetPassword(email);
        } catch (UserNotFoundException e) {
            LOG.warn("User not found (" + email + ").");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return newUserId;
    }

    @Override
    @Transactional
    public void updateUser(Integer userId, String newName, String newMobile, String newStaffId,
                           Boolean isNewAdmin, Boolean isNewUser, Boolean isNewCAdmin, Boolean isNewCUser)
            throws UserNotFoundException, InsufficientAuthorityException, InvalidInputException, GeneralSecurityException {
        SdUserBean currentUser = (SdUserBean) this.getCurrentUserBean();
        if (currentUser.getUserId().equals(userId)) {
            throw new InvalidInputException("Cannot update your own account!");
        }

        // get modifier
        SdUserBean modifier = (SdUserBean) getCurrentUserBean();

        // get current data
        SdUserBean targetUserBean = getUserByUserId(userId);
        Set<GrantedAuthority> authorities = targetUserBean.getAuthorities();
        Boolean isTargetAdmin = hasAnyAuthority(authorities, SdUserGroupEntity.GROUP_ID.ADMIN);
        Boolean isTargetUser = hasAnyAuthority(authorities, SdUserGroupEntity.GROUP_ID.USER);
        Boolean isTargetCAdmin = hasAnyAuthority(authorities, SdUserGroupEntity.GROUP_ID.C_ADMIN);
        Boolean isTargetCUser = hasAnyAuthority(authorities, SdUserGroupEntity.GROUP_ID.C_USER);

        // list to-be-update data
        String name = StringUtils.equals(newName, targetUserBean.getName()) ? null : newName;
        String mobile = StringUtils.equals(newMobile, targetUserBean.getMobile()) ? null : newMobile;
        String staffId = StringUtils.equals(newStaffId, targetUserBean.getStaffId()) ? null : newStaffId;

        Boolean updateIsAdmin = isTargetAdmin == isNewAdmin ? null : isNewAdmin;
        Boolean updateIsUser = isTargetUser == isNewUser ? null : isNewUser;
        Boolean updateIsCAdmin = isTargetCAdmin == isNewCAdmin ? null : isNewCAdmin;
        Boolean updateIsCUser = isTargetCUser == isNewCUser ? null : isNewCUser;

        // encrypt
        byte[] encryptedMobile = StringUtils.isEmpty(mobile) ? null : sdSensitiveDataService.encryptFromString(mobile);
        byte[] encryptedStaffId = StringUtils.isEmpty(staffId) ? null : sdSensitiveDataService.encryptFromString(staffId);

        // update user
        sdUserMapper.updateUser(userId, name, encryptedMobile, encryptedStaffId, modifier.getUserId());

        // update user group
        sdUserGroupService.updateUserGroup(userId, updateIsAdmin, updateIsUser, updateIsCAdmin, updateIsCUser, modifier);

        LOG.info(String.format("Updated user. [name:%b, mobile:%b, staffId:%b]", name != null, mobile != null, staffId != null));
    }


    /**
     * Its public invoking method should be marked @Transactional.
     * Transactional is not marked here, because it takes the invoke of another bean via Spring AOP to trigger rollback.
     * https://stackoverflow.com/questions/4396284/does-spring-transactional-attribute-work-on-a-private-method
     */
    private void updatePassword(Integer userId, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException {
        LOG.info("Trying to update password for user (" + userId + ")...");
        checkValidPassword(rawNewPassword);

        // find current user in db
        SdUserEntity sdUserEntity = sdUserMapper.getUserByUserId(userId, null);
        if (sdUserEntity == null) {
            throw new UserNotFoundException("User not found in database (" + userId + ").");
        }
        String encodedOldPwd = sdUserEntity.getPassword();

        // record old password
        LOG.info("Inserting current password to history...");
        sdUserMapper.insertPasswordHist(userId, encodedOldPwd);

        // check password history
        LOG.info("Checking conflict with password history...");
        List<String> passwordHistoryList = sdUserMapper.getPasswordHistByUserId(userId);
        if (!CollectionUtils.isEmpty(passwordHistoryList)) {
            for (String encodedPastPassword : passwordHistoryList) {
                if (matchPassword(rawNewPassword, encodedPastPassword)) {
                    throw new InvalidPasswordException("Conflict with password history.");
                }
            }
        }

        // update password
        LOG.info("Updating password...");
        String encodedNewPwd = encodePassword(rawNewPassword);
        sdUserMapper.updateUserPassword(userId, encodedNewPwd);
    }

    @Override
    @Transactional
    public void updateUserPwd(Integer userId, String rawOldPassword, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException {
        SdUserEntity sdUserEntity = sdUserMapper.getUserByUserId(userId, null);
        if (sdUserEntity == null) {
            throw new UserNotFoundException("User not found in database (" + userId + ").");
        }

        // check old password
        LOG.info("Checking input matches with old password...");
        String encodedOldPwd = sdUserEntity.getPassword();
        if (!matchPassword(rawOldPassword, encodedOldPwd)) {
            throw new InvalidPasswordException("Incorrect old password.");
        }

        // update new password
        updatePassword(userId, rawNewPassword);

        LOG.info("Updated password for user (" + userId + ").");
    }

    @Override
    @Transactional
    public void resetPwd(String otp, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException, InvalidInputException {
        // check otp
        SdOtpBean sdOtpBean = sdOtpService.getValidResetPwdOtp(otp);
        if (sdOtpBean == null) {
            throw new InvalidInputException("Invalid OTP.");
        }

        Integer userId = sdOtpBean.getUserId();
        LOG.info("Checked password reset OTP for user (" + userId + ").");

        // update new password
        updatePassword(userId, rawNewPassword);

        // expire otp
        sdOtpService.expireOtp(otp);
    }


    @Transactional
    public void activateUserByUsername(String username) throws UserNotFoundException, InvalidInputException {
        SdUserBean currentUser = (SdUserBean) this.getCurrentUserBean();
        if (StringUtils.equals(currentUser.getEmail(), username)) {
            throw new InvalidInputException("Cannot activate your own account!");
        }

        Integer modifyby = getCurrentUserUserId();
        sdUserMapper.resetLoginTriedByUsername(username);
        sdUserMapper.updateUserStatusByUsername(username, SdUserEntity.STATUS.ACTIVE, modifyby);
    }

    public void resetLoginTriedByUsername(String username) {
        sdUserMapper.resetLoginTriedByUsername(username);
    }

    public void addLoginTriedByUsername(String username) {
        sdUserMapper.addLoginTriedByUsername(username);
    }

    public void lockUserByUsername(String username) {
        Integer modifyby = SdUserEntity.SYSTEM.USER_ID;
        sdUserMapper.updateUserStatusByUsername(username, SdUserEntity.STATUS.LOCKED, modifyby);
    }

    public void disableUserByUsername(String username) throws UserNotFoundException, InvalidInputException {
        SdUserBean currentUser = (SdUserBean) this.getCurrentUserBean();
        if (StringUtils.equals(currentUser.getEmail(), username)) {
            throw new InvalidInputException("Cannot deactivate your own account!");
        }

        Integer modifyby = getCurrentUserUserId();
        sdUserMapper.updateUserStatusByUsername(username, SdUserEntity.STATUS.DISABLE, modifyby);
    }

    public boolean isEnabled(BtuUserBean btuUserBean) {
        if (btuUserBean == null) {
            return false;
        }
        return !SdUserEntity.STATUS.DISABLE.equals(btuUserBean.getStatus());
    }

    public boolean isNonLocked(BtuUserBean btuUserBean) {
        if (btuUserBean == null) {
            return false;
        }
        return !SdUserEntity.STATUS.LOCKED.equals(btuUserBean.getStatus());
    }

    private void checkValidPassword(String plaintext) throws InvalidPasswordException {
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 20;

        // check length
        if (StringUtils.isEmpty(plaintext)) {
            throw new InvalidPasswordException("Empty password.");
        } else if (plaintext.length() < MIN_LENGTH) {
            throw new InvalidPasswordException("Password should be at least " + MIN_LENGTH + " char long.");
        } else if (plaintext.length() > MAX_LENGTH) {
            throw new InvalidPasswordException("Password should be at most " + MAX_LENGTH + " char long.");
        }

        final String REGEX_NUM = ".*[0-9].*";
        final String REGEX_ALPHA = ".*[a-zA-Z].*";

        // check pattern
        boolean hasNum = plaintext.matches(REGEX_NUM);
        boolean hasAlpha = plaintext.matches(REGEX_ALPHA);
        if (!hasNum || !hasAlpha) {
            throw new InvalidPasswordException("Password must contain both numeric and alphabetic characters.");
        }
    }

    public Page<SdUserBean> searchUser(Pageable pageable, Integer userId, String email, String name, String userGroupId)
            throws AuthorityNotFoundException {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // make email lower case (**assume email are all lower case)
        email = StringUtils.lowerCase(email);

        // determine company id restriction
        Integer companyId = getCompanyIdRestriction();

        LOG.info(String.format(
                "Searching user with {userId: %s, email: %s, name: %s, companyId: %s, userGroupId: %s}",
                userId, email, name, companyId, userGroupId));

        // get total count
        Integer totalCount = sdUserMapper.countSearchUser(companyId, userId, email, name, userGroupId);

        // get content
        List<SdUserEntity> sdUserEntityList = sdUserMapper.searchUser(offset, pageSize, companyId, userId, email, name, userGroupId);
        List<SdUserBean> sdUserBeanList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(sdUserEntityList)) {
            for (SdUserEntity sdUserEntity : sdUserEntityList) {
                SdUserBean sdUserBean = new SdUserBean();
                sdUserBeanPopulator.populate(sdUserEntity, sdUserBean);
                sdUserBeanList.add(sdUserBean);
            }
        }

        return new PageImpl<>(sdUserBeanList, pageable, totalCount);
    }

    /**
     * Return company id that the current user belongs and has access to,
     * return null if there is no limitation.
     */
    public Integer getCompanyIdRestriction() throws AuthorityNotFoundException {
        SdUserBean currentUser;
        try {
            currentUser = (SdUserBean) getCurrentUserBean();
        } catch (UserNotFoundException e) {
            throw new AuthorityNotFoundException(e.getMessage());
        }

        Integer companyIdRestriction = currentUser.getCompanyId();
        if (isInternalUser()) {
            // internal user has no restriction over company id (independent of user id)
            return null;
        }

        return companyIdRestriction;
    }

    /**
     * Return user id that the current user has access to,
     * return null if there is no limitation.
     */
    public Integer getUserIdRestriction() throws AuthorityNotFoundException {
        SdUserBean currentUser;
        try {
            currentUser = (SdUserBean) getCurrentUserBean();
        } catch (UserNotFoundException e) {
            throw new AuthorityNotFoundException(e.getMessage());
        }

        Integer userIdRestriction = currentUser.getUserId();
        if (isAdminUser()) {
            // admin user has no restriction over user id (independent of company id)
            return null;
        }

        return userIdRestriction;
    }

    @Override
    public boolean isInternalUser() {
        return hasAnyAuthority(SdUserGroupEntity.GROUP_ID.ROOT, SdUserGroupEntity.GROUP_ID.ADMIN, SdUserGroupEntity.GROUP_ID.USER);
    }

    @Override
    public boolean isAdminUser() {
        return hasAnyAuthority(SdUserGroupEntity.GROUP_ID.ROOT, SdUserGroupEntity.GROUP_ID.ADMIN, SdUserGroupEntity.GROUP_ID.C_ADMIN);
    }


    public void requestResetPassword(String username) throws UserNotFoundException, MessagingException {
        SdUserEntity sdUserEntity = sdUserMapper.getUserByEmail(username);
        if (sdUserEntity == null) {
            throw new UserNotFoundException();
        }

        Integer userId = sdUserEntity.getUserId();
        String otp = sdOtpService.generatePwdResetOtp(userId);
        boolean isNewlyCreated = sdUserEntity.getPasswordModifydate() == null;
        LOG.info("Generated password OTP successfully for user " + username + ".");


        // send otp email
        if (isNewlyCreated) {
            String recipient = sdUserEntity.getEmail();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(SdEmailBean.EMAIL_BASIC_RECIPIENT_NAME, sdUserEntity.getName());
            dataMap.put(SdEmailBean.INIT_PW_EMAIL.OTP, otp);

            sdEmailService.send(SdEmailBean.INIT_PW_EMAIL.TEMPLATE_ID, recipient, dataMap);
        } else {
            String recipient = sdUserEntity.getEmail();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(SdEmailBean.EMAIL_BASIC_RECIPIENT_NAME, sdUserEntity.getName());
            dataMap.put(SdEmailBean.RESET_PW_EMAIL.OTP, otp);

            sdEmailService.send(SdEmailBean.RESET_PW_EMAIL.TEMPLATE_ID, recipient, dataMap);
        }
    }
}
