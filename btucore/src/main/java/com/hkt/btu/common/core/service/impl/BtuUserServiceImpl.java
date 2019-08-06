package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuUserEntity;
import com.hkt.btu.common.core.dao.entity.BtuUserGroupEntity;
import com.hkt.btu.common.core.dao.mapper.BtuUserGroupMapper;
import com.hkt.btu.common.core.dao.mapper.BtuUserMapper;
import com.hkt.btu.common.core.exception.*;
import com.hkt.btu.common.core.service.*;
import com.hkt.btu.common.core.service.bean.BtuEmailBean;
import com.hkt.btu.common.core.service.bean.BtuOtpBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.populator.BtuUserBeanPopulator;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * User Service Implementation for usage in Spring Security
 */
public class BtuUserServiceImpl implements BtuUserService {
    private static final Logger LOG = LogManager.getLogger(BtuUserService.class);

    @Resource
    BtuUserMapper userMapper;

    @Resource
    BtuUserGroupMapper userGroupMapper;

    @Resource(name = "userBeanPopulator")
    BtuUserBeanPopulator userBeanPopulator;

    @Resource(name = "otpService")
    BtuOtpService otpService;

    @Resource(name = "userGroupService")
    BtuUserGroupService userGroupService;

    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;

    @Resource(name = "emailService")
    BtuEmailService emailService;

    @Resource(name = "btuPasswordEncoder")
    BCryptPasswordEncoder btuPasswordEncoder;

    @Override
    public BtuUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof BtuUser) {
            return (BtuUser) authentication.getPrincipal();
        }

        LOG.warn("Current user bean not found.");
        return null;
    }

    @Override
    public BtuUserBean getCurrentUserBean() throws UserNotFoundException {
        BtuUser btuUser = this.getCurrentUser();
        if (ObjectUtils.isEmpty(btuUser) || ObjectUtils.isEmpty(btuUser.getUserBean())) {
            throw new UserNotFoundException("No UserBean found in security context!");
        }
        btuUser.getUserBean().setUserId(30);
        return btuUser.getUserBean();
    }


    public BtuUserBean getUserBeanByUsername(String username) {
        // make email lower case
        String email = StringUtils.lowerCase(username);

        // get user data
        BtuUserEntity sdUserEntity = userMapper.getUserByEmail(email);
        if (sdUserEntity == null) {
            return null;
        }

        // get user group data
        List<BtuUserGroupEntity> groupEntityList = userGroupMapper.getUserGroupByUserId(sdUserEntity.getUserId());

        // construct bean
        BtuUserBean userBean = new BtuUserBean();
        userBeanPopulator.populate(sdUserEntity, userBean);
        userBeanPopulator.populate(groupEntityList, userBean);

        return userBean;
    }

    public void resetLoginTriedByUsername(String username) {
        userMapper.resetLoginTriedByUsername(username);
    }

    public void addLoginTriedByUsername(String username) {
        userMapper.addLoginTriedByUsername(username);
    }

    public void lockUserByUsername(String username) {
        Integer modifyby = BtuUserEntity.SYSTEM.USER_ID;
        userMapper.updateUserStatusByUsername(username, BtuUserEntity.STATUS.LOCKED, modifyby);
    }

    public void activateUserByUsername(String username) {
        BtuUserBean currentUser =this.getCurrentUserBean();
        if (StringUtils.equals(currentUser.getEmail(), username)) {
            throw new InvalidInputException("Cannot activate your own account!");
        }

        Integer modifyby = getCurrentUserUserId();
        userMapper.resetLoginTriedByUsername(username);
        userMapper.updateUserStatusByUsername(username, BtuUserEntity.STATUS.ACTIVE, modifyby);
    }

    public boolean isEnabled(BtuUserBean btuUserBean) {
        if (ObjectUtils.isEmpty(btuUserBean)) {
            return false;
        }
        return !BtuUserEntity.STATUS.DISABLE.equals(btuUserBean.getStatus());
    }

    public boolean isNonLocked(BtuUserBean btuUserBean) {
        if (ObjectUtils.isEmpty(btuUserBean)) {
            return false;
        }
        return !BtuUserEntity.STATUS.LOCKED.equals(btuUserBean.getStatus());
    }

    @Override
    public boolean hasAnyAuthority(String... targetAuthorities) {
        // get security context
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return false;
        }

        return hasAnyAuthority(authentication.getAuthorities(), targetAuthorities);
    }

    @Override
    public Integer getCurrentUserUserId() throws UserNotFoundException {
        return getCurrentUserBean().getUserId();
    }

    /**
     * Return company id that the current user belongs and has access to,
     * return null if there is no limitation.
     */
    @Override
    public Integer getCompanyIdRestriction() throws AuthorityNotFoundException {
        BtuUserBean userBean;
        try {
            userBean = getCurrentUserBean();
        } catch (UserNotFoundException e) {
            throw new AuthorityNotFoundException(e.getMessage());
        }
        Integer companyIdRestriction = userBean.getCompanyId();
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
    @Override
    public Integer getUserIdRestriction() throws AuthorityNotFoundException {
        BtuUserBean userBean;
        try {
            userBean = getCurrentUserBean();
        } catch (UserNotFoundException e) {
            throw new AuthorityNotFoundException(e.getMessage());
        }
        Integer userIdRestriction = userBean.getUserId();
        if (isAdminUser()) {
            // admin user has no restriction over user id (independent of company id)
            return null;
        }
        return userIdRestriction;
    }

    @Override
    public boolean isInternalUser() {
        return hasAnyAuthority(BtuUserGroupEntity.GROUP_ID.ROOT, BtuUserGroupEntity.GROUP_ID.ADMIN, BtuUserGroupEntity.GROUP_ID.USER);
    }

    @Override
    public boolean isAdminUser() {
        return hasAnyAuthority(BtuUserGroupEntity.GROUP_ID.ROOT, BtuUserGroupEntity.GROUP_ID.ADMIN, BtuUserGroupEntity.GROUP_ID.C_ADMIN);
    }

    @Override
    public BtuUserBean getUserByUserId(Integer userId) throws UserNotFoundException {
        if (userId == null) {
            throw new UserNotFoundException("Empty user id input.");
        }

        // determine company id restriction
        Integer companyId = getCompanyIdRestriction();

        // get user data
        BtuUserEntity sdUserEntity = userMapper.getUserByUserId(userId, companyId);
        if (sdUserEntity == null) {
            throw new UserNotFoundException("Cannot find user with id " + userId + ".");
        }

        // get user group data
        List<BtuUserGroupEntity> groupEntityList = userGroupMapper.getUserGroupByUserId(userId);

        // construct bean
        BtuUserBean userBean = new BtuUserBean();
        userBeanPopulator.populate(sdUserEntity, userBean);
        userBeanPopulator.populate(groupEntityList, userBean);

        return userBean;

    }

    @Override
    public Page<BtuUserBean> searchUser(Pageable pageable, Integer userId, String email, String name, String userGroupId) {
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
        Integer totalCount = userMapper.countSearchUser(companyId, userId, email, name, userGroupId);

        // get content
        List<BtuUserEntity> sdUserEntityList = userMapper.searchUser(offset, pageSize, companyId, userId, email, name, userGroupId);
        List<BtuUserBean> sdUserBeanList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(sdUserEntityList)) {
            for (BtuUserEntity sdUserEntity : sdUserEntityList) {
                BtuUserBean userBean = new BtuUserBean();
                userBeanPopulator.populate(sdUserEntity, userBean);
                sdUserBeanList.add(userBean);
            }
        }

        return new PageImpl<>(sdUserBeanList, pageable, totalCount);
    }

    @Override
    public void updateUserPwd(Integer userId, String rawOldPassword, String rawNewPassword) throws UserNotFoundException, InvalidPasswordException {
        BtuUserEntity sdUserEntity = userMapper.getUserByUserId(userId, null);
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
        BtuUserEntity sdUserEntity = userMapper.getUserByUserId(userId, null);
        if (sdUserEntity == null) {
            throw new UserNotFoundException("User not found in database (" + userId + ").");
        }
        String encodedOldPwd = sdUserEntity.getPassword();

        // record old password
        LOG.info("Inserting current password to history...");
        userMapper.insertPasswordHist(userId, encodedOldPwd);

        // check password history
        LOG.info("Checking conflict with password history...");
        List<String> passwordHistoryList = userMapper.getPasswordHistByUserId(userId);
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
        userMapper.updateUserPassword(userId, encodedNewPwd);
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

    @Override
    public void resetPwd(String otp, String rawNewPassword) throws UserNotFoundException, InvalidPasswordException, InvalidInputException {
        // check otp
        BtuOtpBean sdOtpBean = otpService.getValidResetPwdOtp(otp);
        if (sdOtpBean == null) {
            throw new InvalidInputException("Invalid OTP.");
        }

        Integer userId = sdOtpBean.getUserId();
        LOG.info("Checked password reset OTP for user (" + userId + ").");

        // update new password
        updatePassword(userId, rawNewPassword);

        // expire otp
        otpService.expireOtp(otp);
    }

    @Override
    public void disableUserByUsername(String username) throws UserNotFoundException, InvalidInputException {
        BtuUserBean currentUser = this.getCurrentUserBean();
        if (StringUtils.equals(currentUser.getEmail(), username)) {
            throw new InvalidInputException("Cannot deactivate your own account!");
        }
        Integer modifyby = getCurrentUserUserId();
        userMapper.updateUserStatusByUsername(username, BtuUserEntity.STATUS.DISABLE, modifyby);

    }

    @Override
    public Integer createUser(String name, String mobile, String email, String staffId, Integer companyId, List<String> groupIdList) throws DuplicateUserEmailException, UserNotFoundException, GeneralSecurityException {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidInputException("Empty user name.");
        } else if (StringUtils.isEmpty(email)) {
            throw new InvalidInputException("Empty email.");
        }

        // get current user user id
        Integer createby = getCurrentUserUserId();

        // check current user has right to create user of user group
        boolean isEligibleUserGroup = userGroupService.isEligibleToGrantUserGroup(groupIdList);
        if (!isEligibleUserGroup) {
            LOG.warn("Ineligible to create user of selected user group (" + groupIdList + ") by user (" + createby + ").");
            throw new InvalidInputException("Invalid user group.");
        }


        // check email duplicated
        BtuUserEntity userEntity = userMapper.getUserByEmail(email);
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
        byte[] encryptedMobile = StringUtils.isEmpty(mobile) ? null : sensitiveDataService.encryptFromString(mobile);
        byte[] encryptedStaffId = StringUtils.isEmpty(staffId) ? null : sensitiveDataService.encryptFromString(staffId);

        // prepare entity
        BtuUserEntity sdUserEntity = new BtuUserEntity();
        sdUserEntity.setName(name);
        sdUserEntity.setStatus(BtuUserEntity.STATUS.ACTIVE);
        sdUserEntity.setMobile(encryptedMobile);
        sdUserEntity.setEmail(email);
        sdUserEntity.setCompanyId(companyId);
        sdUserEntity.setStaffId(encryptedStaffId);
        sdUserEntity.setPassword(encodedPassword);
        sdUserEntity.setCreateby(createby);

        // create user in db
        userMapper.insertUser(sdUserEntity);

        // get new user id
        Integer newUserId = sdUserEntity.getUserId();

        // create user group relation in db
        if (!CollectionUtils.isEmpty(groupIdList)) {
            for (String groupId : groupIdList) {
                userGroupMapper.insertUserUserGroup(newUserId, groupId, createby);
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
    public void updateUser(Integer userId, String newName, String newMobile, String newStaffId, Boolean isNewAdmin, Boolean isNewUser, Boolean isNewCAdmin, Boolean isNewCUser) throws UserNotFoundException, InsufficientAuthorityException, GeneralSecurityException {
        BtuUserBean currentUser = getCurrentUserBean();
        if (currentUser.getUserId().equals(userId)) {
            throw new InvalidInputException("Cannot update your own account!");
        }

        // get modifier
        BtuUserBean modifier = getCurrentUserBean();

        // get current data
        BtuUserBean targetUserBean = getUserByUserId(userId);
        Set<GrantedAuthority> authorities = targetUserBean.getAuthorities();
        Boolean isTargetAdmin = hasAnyAuthority(authorities, BtuUserGroupEntity.GROUP_ID.ADMIN);
        Boolean isTargetUser = hasAnyAuthority(authorities, BtuUserGroupEntity.GROUP_ID.USER);
        Boolean isTargetCAdmin = hasAnyAuthority(authorities, BtuUserGroupEntity.GROUP_ID.C_ADMIN);
        Boolean isTargetCUser = hasAnyAuthority(authorities, BtuUserGroupEntity.GROUP_ID.C_USER);

        // list to-be-update data
        String name = StringUtils.equals(newName, targetUserBean.getName()) ? null : newName;
        String mobile = StringUtils.equals(newMobile, targetUserBean.getMobile()) ? null : newMobile;
        String staffId = StringUtils.equals(newStaffId, targetUserBean.getStaffId()) ? null : newStaffId;

        Boolean updateIsAdmin = isTargetAdmin == isNewAdmin ? null : isNewAdmin;
        Boolean updateIsUser = isTargetUser == isNewUser ? null : isNewUser;
        Boolean updateIsCAdmin = isTargetCAdmin == isNewCAdmin ? null : isNewCAdmin;
        Boolean updateIsCUser = isTargetCUser == isNewCUser ? null : isNewCUser;

        // encrypt
        byte[] encryptedMobile = StringUtils.isEmpty(mobile) ? null : sensitiveDataService.encryptFromString(mobile);
        byte[] encryptedStaffId = StringUtils.isEmpty(staffId) ? null : sensitiveDataService.encryptFromString(staffId);

        // update user
        userMapper.updateUser(userId, name, encryptedMobile, encryptedStaffId, modifier.getUserId());

        // update user group
        userGroupService.updateUserGroup(userId, updateIsAdmin, updateIsUser, updateIsCAdmin, updateIsCUser, modifier);

        LOG.info(String.format("Updated user. [name:%b, mobile:%b, staffId:%b]", name != null, mobile != null, staffId != null));

    }

    @Override
    public void requestResetPassword(String username) throws UserNotFoundException, MessagingException {
        BtuUserEntity useren = userMapper.getUserByEmail(username);
        if (useren == null) {
            throw new UserNotFoundException();
        }

        Integer userId = useren.getUserId();
        String otp = otpService.generatePwdResetOtp(userId);
        boolean isNewlyCreated = useren.getPasswordModifydate() == null;
        LOG.info("Generated password OTP successfully for user " + username + ".");


        // send otp email
        if (isNewlyCreated) {
            String recipient = useren.getEmail();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(BtuEmailBean.EMAIL_BASIC_RECIPIENT_NAME, useren.getName());
            dataMap.put(BtuEmailBean.INIT_PW_EMAIL.OTP, otp);

            emailService.send(BtuEmailBean.INIT_PW_EMAIL.TEMPLATE_ID, recipient, dataMap);
        } else {
            String recipient = useren.getEmail();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(BtuEmailBean.EMAIL_BASIC_RECIPIENT_NAME, useren.getName());
            dataMap.put(BtuEmailBean.RESET_PW_EMAIL.OTP, otp);

            emailService.send(BtuEmailBean.RESET_PW_EMAIL.TEMPLATE_ID, recipient, dataMap);
        }

    }

    protected boolean hasAnyAuthority(Collection<? extends GrantedAuthority> authorities, String... targetAuthorities) {
        // find matching auth
        for (String targetAuth : targetAuthorities) {
            for (GrantedAuthority grantedAuthority : authorities) {
                if (!(grantedAuthority instanceof SimpleGrantedAuthority)) {
                    continue;
                }

                SimpleGrantedAuthority existingAuth = (SimpleGrantedAuthority) grantedAuthority;
                if (StringUtils.equals(targetAuth, existingAuth.getAuthority())) {
                    return true;
                }
            }
        }

        return false;
    }

    protected String encodePassword(CharSequence plaintext) {
        return btuPasswordEncoder.encode(plaintext);
    }

    protected boolean matchPassword(String rawPassword, String encodedPassword) {
        return btuPasswordEncoder.matches(rawPassword, encodedPassword);
    }

}
