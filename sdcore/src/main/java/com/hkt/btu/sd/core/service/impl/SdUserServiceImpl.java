package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.BtuLdapService;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.impl.BtuUserServiceImpl;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdOtpEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserGroupEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserMapper;
import com.hkt.btu.sd.core.dao.mapper.SdUserRoleMapper;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.*;
import com.hkt.btu.sd.core.service.bean.SdCreateResultBean;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

public class SdUserServiceImpl extends BtuUserServiceImpl implements SdUserService {
    private static final Logger LOG = LogManager.getLogger(SdUserServiceImpl.class);

    @Resource
    SdUserMapper sdUserMapper;

    @Resource(name = "userBeanPopulator")
    SdUserBeanPopulator sdUserBeanPopulator;

    @Resource(name = "otpService")
    SdOtpService sdOtpService;
    @Resource(name = "emailService")
    SdEmailService sdEmailService;

    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService btuSensitiveDataService;

    @Resource(name = "ldapService")
    BtuLdapService btuLdapService;

    @Resource(name = "roleService")
    SdUserRoleService userRoleService;

    @Resource
    SdUserRoleMapper sdUserRoleMapper;

    @Override
    public BtuUserBean getCurrentUserBean() throws UserNotFoundException {
        BtuUser btuUser = this.getCurrentUser();
        if (btuUser == null) {
            throw new UserNotFoundException("No UserBean found in security context!");
        } else if (btuUser.getUserBean() instanceof SdUserBean) {
            return btuUser.getUserBean();
        }
        throw new UserNotFoundException("No SdUserBean found in security context!");
    }


    @Override
    public BtuUserBean getUserBeanByUsername(String username) {

        // get user data
        SdUserEntity sdUserEntity = null;
        if (username.contains(SdUserBean.CREATE_USER_PREFIX.PCCW_HKT_USER) ||
                username.contains(SdUserBean.CREATE_USER_PREFIX.NON_PCCW_HKT_USER)) {
            sdUserEntity = sdUserMapper.getLdapUserByUserId(username);
        } else if (username.contains("@")) {
            sdUserEntity = sdUserMapper.getUserByEmail(username);
        } else {
            sdUserEntity = sdUserMapper.getLdapUserByUserId(username);
        }
        if (sdUserEntity == null) {
            return null;
        }

        // get user role data
        List<SdUserRoleEntity> userRole = sdUserRoleMapper.getUserRoleByUserIdAndStatus(sdUserEntity.getUserId(), SdUserRoleEntity.ACTIVE_ROLE_STATUS);
        if (CollectionUtils.isEmpty(userRole)) {
            return null;
        }

        List<SdUserRoleEntity> results = new LinkedList<>();

        for (SdUserRoleEntity roleEntity : userRole) {
            results.add(roleEntity);
            if (StringUtils.isNotEmpty(roleEntity.getParentRoleId())) {
                List<SdUserRoleEntity> parentRoleByRoleId = userRoleService.getParentRoleByRoleId(roleEntity.getParentRoleId());
                if (CollectionUtils.isNotEmpty(parentRoleByRoleId)) {
                    results.addAll(parentRoleByRoleId);
                }
            }
        }

        // construct bean
        SdUserBean userBean = new SdUserBean();
        sdUserBeanPopulator.populate(sdUserEntity, userBean);
        sdUserBeanPopulator.populate(results, userBean);

        return userBean;
    }

    @Override
    public String getCurrentUserUserId() {
        SdUserBean sdUserBean = (SdUserBean) getCurrentUserBean();
        return sdUserBean.getUserId();
    }

    @Override
    public SdUserBean getUserByUserId(String userId) throws UserNotFoundException,InsufficientAuthorityException {
        if (userId == null) {
            throw new UserNotFoundException("Empty user id input.");
        }


        // TH__TEAM_B
        Set<GrantedAuthority> authorities = getCurrentUserBean().getAuthorities();

        // get user data
        SdUserEntity sdUserEntity = sdUserMapper.getUserByUserId(userId);
        if (sdUserEntity == null) {
            throw new UserNotFoundException("Cannot find user with id " + userId + ".");
        }

        // get user group data
        List<SdUserRoleEntity> roleEntityList = sdUserRoleMapper.getUserRoleByUserIdAndStatus(userId, SdUserRoleEntity.ACTIVE_ROLE_STATUS);

        // get roleId
        List<String> roleIdList = roleEntityList.stream().map(SdUserRoleEntity::getRoleId).collect(Collectors.toList());

        userRoleService.checkUserRole(authorities,roleIdList);

        // construct bean
        SdUserBean userBean = new SdUserBean();
        sdUserBeanPopulator.populate(sdUserEntity, userBean);

        sdUserBeanPopulator.populate(roleEntityList, userBean);

        return userBean;
    }


    @Transactional
    public String createLdapUser(String name, String mobile, String employeeNumber,
                                 String ldapDomain, List<String> roleIdList)
            throws DuplicateUserEmailException, UserNotFoundException {
        try {
            // get current userId for CreateBy
            String createBy = getCurrentUserUserId();
            // Check if there is a duplicate name
            SdUserEntity sdUserEntity = sdUserMapper.getLdapUserByUserId(name);
            if (sdUserEntity != null) {
                throw new DuplicateUserEmailException("User already exist.");
            }
            // check current user has right to create user of user role
            boolean isEligibleUserGroup = userRoleService.isEligibleToGrantUserRole(roleIdList);
            if (!isEligibleUserGroup) {
                LOG.warn("Ineligible to create user of selected user role (" + roleIdList + ") by user (" + createBy + ").");
                throw new InvalidInputException("Invalid user role.");
            }

            // Data input
            SdUserEntity userEntity = new SdUserEntity();
            userEntity.setName(name);
            userEntity.setCreateby(createBy);
            userEntity.setUserId(employeeNumber);
            userEntity.setMobile(mobile);
            userEntity.setStatus(SdUserEntity.STATUS.ACTIVE);
            userEntity.setLdapDomain(ldapDomain);

            // create user in db
            sdUserMapper.insertUser(userEntity);

            // create user role relation in db
            if (!CollectionUtils.isEmpty(roleIdList)) {
                for (String roleId : roleIdList) {
                    sdUserRoleMapper.insertUserUserRole(employeeNumber, roleId);
                }
            }

            LOG.info("User (id: " + employeeNumber + ") created.");

            return employeeNumber;
        } catch (Exception e) {
            LOG.warn(e.getMessage());
            throw new DuplicateUserEmailException("User already exists.");
        }
    }

    @Override
    public SdCreateResultBean createUser(String userId, String name, String mobile, String email, List<String> roleIdList)
            throws DuplicateUserEmailException, UserNotFoundException {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidInputException("Empty user name.");
        }

        // get current user user id
        String createby = getCurrentUserUserId();

        // check current user has right to create user of user role
        boolean isEligibleUserGroup = userRoleService.isEligibleToGrantUserRole(roleIdList);
        if (!isEligibleUserGroup) {
            LOG.warn("Ineligible to create user of selected user role (" + roleIdList + ") by user (" + createby + ").");
            throw new InvalidInputException("Invalid user role.");
        }

        // check email duplicated
        SdUserEntity userEntity = sdUserMapper.getUserByEmail(email);
        if (userEntity != null) {
            throw new DuplicateUserEmailException();
        }

        // generate dummy password
        UUID uuid = UUID.randomUUID();
        String password = uuid.toString();
        String encodedPassword = encodePassword(password);

        // prepare entity
        SdUserEntity sdUserEntity = new SdUserEntity();

        sdUserEntity.setUserId(userId);
        sdUserEntity.setName(name);
        sdUserEntity.setStatus(SdUserEntity.STATUS.ACTIVE);

        sdUserEntity.setMobile(mobile);
        sdUserEntity.setEmail(email);
        sdUserEntity.setPassword(encodedPassword);
        sdUserEntity.setCreateby(createby);

        // create user in db
        sdUserMapper.insertUser(sdUserEntity);

        // create user group relation in db
        if (!CollectionUtils.isEmpty(roleIdList)) {
            for (String groupId : roleIdList) {
                sdUserRoleMapper.insertUserUserRole(userId, groupId);
            }
        }

        LOG.info("User (id: " + userId + ") " + email + " created.");


        // send init password email
        try {
            if (StringUtils.isNotEmpty(email)) {
                password = null;
                requestResetPassword(email);
            }
        } catch (UserNotFoundException e) {
            LOG.warn("User not found (" + email + ").");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return SdCreateResultBean.of(userId, password);
    }


    @Override
    @Transactional
    public void updateUser(String userId, String newName, String newMobile, List<String> userRoleIdList) throws UserNotFoundException, InsufficientAuthorityException, GeneralSecurityException {
        SdUserBean currentUser = (SdUserBean) this.getCurrentUserBean();
        if (currentUser.getUserId().equals(userId)) {
            throw new InvalidInputException("Cannot update your own account!");
        }

        // get modifier
        SdUserBean modifier = (SdUserBean) getCurrentUserBean();
        SdUserBean targetUserBean = getUserByUserId(userId);
        String name = StringUtils.equals(newName, targetUserBean.getName()) ? null : newName;
        String mobile = StringUtils.equals(newMobile, targetUserBean.getMobile()) ? null : newMobile;
        String encryptedMobile = StringUtils.isEmpty(mobile) ? null : mobile;

        userRoleService.checkUserRole(modifier.getAuthorities(), userRoleIdList);

        sdUserMapper.updateUser(userId, name, encryptedMobile, modifier.getUserId());

        // update user role
        userRoleService.updateUserRoleByUserId(userId, userRoleIdList);

        LOG.info(String.format("Updated user. [name:%b, mobile:%b]", name != null, mobile != null));
    }


    /**
     * Its public invoking method should be marked @Transactional.
     * Transactional is not marked here, because it takes the invoke of another bean via Spring AOP to trigger rollback.
     * https://stackoverflow.com/questions/4396284/does-spring-transactional-attribute-work-on-a-private-method
     */
    private void updatePassword(String userId, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException {
        LOG.info("Trying to update password for user (" + userId + ")...");
        checkValidPassword(rawNewPassword);

        // find current user in db
        SdUserEntity sdUserEntity = sdUserMapper.getUserByUserId(userId);
        if (sdUserEntity == null) {
            throw new UserNotFoundException("User not found in database (" + userId + ").");
        }
        String encodedOldPwd = sdUserEntity.getPassword();

        if (encodedOldPwd != null) {
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
        }

        // update password
        LOG.info("Updating password...");
        String encodedNewPwd = encodePassword(rawNewPassword);
        sdUserMapper.updateUserPassword(userId, encodedNewPwd);
    }

    @Override
    @Transactional
    public void updateUserPwd(String userId, String rawOldPassword, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException {
        SdUserEntity sdUserEntity = sdUserMapper.getUserByUserId(userId);
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
            throws UserNotFoundException, InvalidPasswordException, InvalidOtpException {
        // check otp
        SdOtpBean sdOtpBean = sdOtpService.getValidOtp(otp);
        if (sdOtpBean == null) {
            throw new InvalidOtpException("Invalid OTP.");
        }

        String userId = sdOtpBean.getUserId();
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

        String modifyby = getCurrentUserUserId();
        sdUserMapper.resetLoginTriedByUsername(username);
        sdUserMapper.updateUserStatusByUsername(username, SdUserEntity.STATUS.ACTIVE, modifyby);
    }

    @Override
    @Transactional
    public BtuUserBean verifyLdapUser(BtuUser user, BtuUserBean userDetailBean) {
        if (StringUtils.isEmpty(userDetailBean.getEmail()) &&
                StringUtils.isNotEmpty(userDetailBean.getLdapDomain())) {
            BtuUserBean btuUserBean = super.verifyLdapUser(user, userDetailBean);
            String userId = userDetailBean.getUserId();
            String email = btuUserBean.getEmail();
            String username = btuUserBean.getUsername();
            if (StringUtils.isNotEmpty(email)) {
                sdUserMapper.updateLdapUser(userId, null, email);
            }
            if (StringUtils.isNotEmpty(username)) {
                sdUserMapper.updateLdapUser(userId, username, null);
            }
        }
        return null;
    }

    public void resetLoginTriedByUsername(String username) {
        sdUserMapper.resetLoginTriedByUsername(username);
    }

    public void addLoginTriedByUsername(String username) {
        sdUserMapper.addLoginTriedByUsername(username);
    }

    public void lockUserByUsername(String username) {
        String modifyby = SdUserEntity.SYSTEM.USER_ID;
        sdUserMapper.updateUserStatusByUsername(username, SdUserEntity.STATUS.LOCKED, modifyby);
    }

    public void disableUserByUsername(String username) throws UserNotFoundException, InvalidInputException {
        SdUserBean currentUser = (SdUserBean) this.getCurrentUserBean();
        if (StringUtils.equals(currentUser.getEmail(), username)) {
            throw new InvalidInputException("Cannot deactivate your own account!");
        }

        String modifyby = getCurrentUserUserId();
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

    public Page<SdUserBean> searchUser(Pageable pageable, String userId, String email, String name)
            throws AuthorityNotFoundException {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // make email lower case (**assume email are all lower case)
        email = StringUtils.lowerCase(email);

        // get Current User Role
        SdUserBean currentUserBean = (SdUserBean) getCurrentUserBean();

        LOG.info(String.format(
                "Searching user with {userId: %s, email: %s, name: %s}",
                userId, email, name));

        Set<GrantedAuthority> authorities = currentUserBean.getAuthorities();
        Set<SdUserEntity> sdUserEntitySet = new HashSet<>();

        Integer totalCount = 0;

        for (GrantedAuthority authority : authorities) {
            if (authority instanceof SimpleGrantedAuthority) {
                String roleId = authority.getAuthority();
                if (SdUserRoleEntity.SYS_ADMIN.equals(roleId)) {
                    totalCount += sdUserMapper.countSearchUser(roleId, userId, email, name);
                    sdUserEntitySet.addAll(sdUserMapper.searchUser(offset, pageSize, roleId, userId, email, name));
                    break;
                }
                if (roleId.contains(SdUserRoleEntity.TEAM_HEAD_INDICATOR)) {
                    totalCount += sdUserMapper.countSearchUser(roleId, userId, email, name);
                    sdUserEntitySet.addAll(sdUserMapper.searchUser(offset, pageSize, roleId, userId, email, name));
                }
            }
        }


        List<SdUserBean> sdUserBeanList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(sdUserEntitySet)) {
            for (SdUserEntity sdUserEntity : sdUserEntitySet) {
                SdUserBean sdUserBean = new SdUserBean();
                sdUserBeanPopulator.populate(sdUserEntity, sdUserBean);
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
    public String getUserIdRestriction() throws AuthorityNotFoundException {
        SdUserBean currentUser;
        try {
            currentUser = (SdUserBean) getCurrentUserBean();
        } catch (UserNotFoundException e) {
            throw new AuthorityNotFoundException(e.getMessage());
        }

        String userIdRestriction = currentUser.getUserId();
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

    @Override
    @Transactional
    public String changeUserTypeToPCCWOrHktUser(String oldUserId, String name, String mobile, String employeeNumber, String email)
            throws InvalidInputException, UserNotFoundException {
        // Determine if it is already a PCCW/HKT user. UserId starts with T
        if (oldUserId.contains(SdUserBean.CREATE_USER_PREFIX.PCCW_HKT_USER)) {
            throw new InvalidInputException("You are already PCCW/HKT user.");
        }
        SdUserBean currentUserBean = (SdUserBean) getCurrentUserBean();
        if (currentUserBean.getUserId().equals(oldUserId)) {
            throw new InvalidInputException("Cannot change you own account.");
        }
        SdUserEntity user = sdUserMapper.getLdapUserByUserId(oldUserId);
        if (user == null) {
            throw new UserNotFoundException("User not Exist.");
        }

        String userId = SdUserBean.CREATE_USER_PREFIX.PCCW_HKT_USER + employeeNumber;
        List<SdUserRoleEntity> userRoleByUserId = sdUserRoleMapper.getUserRoleByUserId(oldUserId);

        sdUserMapper.changeUserType(oldUserId, name, mobile, userId, null, email, currentUserBean.getUserId());

        // Change USER_ID IN USER_USER_ROLE
        sdUserRoleMapper.deleteUserRoleByUserId(oldUserId);
        for (SdUserRoleEntity entity : userRoleByUserId) {
            sdUserRoleMapper.insertUserUserRole(userId, entity.getRoleId());
        }

        return userId;
    }

    @Override
    @Transactional
    public String changeUserTypeToLdapUser(String oldUserId, String name, String mobile, String employeeNumber, String ldapDomain)
            throws InvalidInputException, UserNotFoundException {
        SdUserEntity entity = sdUserMapper.getLdapUserByUserId(employeeNumber);
        if (entity != null) {
            throw new InvalidInputException("The EmployeeNumber already exists.");
        }
        SdUserEntity user = sdUserMapper.getLdapUserByUserId(oldUserId);
        if (StringUtils.isNotEmpty(user.getLdapDomain())) {
            throw new InvalidInputException("You are already LDAP user.");
        }
        // Get CurrentUser For ModifyBy
        SdUserBean currentUserBean = (SdUserBean) getCurrentUserBean();
        // Get the User Role
        List<SdUserRoleEntity> userRoleByUserId = sdUserRoleMapper.getUserRoleByUserId(oldUserId);

        // Update User Data
        sdUserMapper.changeUserType(oldUserId, name, mobile, employeeNumber, ldapDomain, null, currentUserBean.getUserId());

        // Change USER_ID IN USER_USER_ROLE
        sdUserRoleMapper.deleteUserRoleByUserId(oldUserId);
        for (SdUserRoleEntity userEntity : userRoleByUserId) {
            sdUserRoleMapper.insertUserUserRole(employeeNumber, userEntity.getRoleId());
        }

        return employeeNumber;
    }

    public void requestResetPassword(String username) throws UserNotFoundException, MessagingException {
        // make email lower case
        String email = StringUtils.lowerCase(username);

        // get user data
        SdUserEntity sdUserEntity = sdUserMapper.getUserByEmail(email);
        if (sdUserEntity == null) {
            throw new UserNotFoundException();
        }
        String userId = sdUserEntity.getUserId();

        // reject LDAP user to reset password
        String ldapDomain = sdUserEntity.getLdapDomain();
        if (StringUtils.isNotEmpty(ldapDomain)) {
            throw new InvalidUserTypeException("LDAP users are not allowed to reset passwords.");
        }

        boolean isNewlyCreated = sdUserEntity.getPasswordModifydate() == null;
        if (isNewlyCreated) {
            // valid init password otp
            SdOtpBean sdOtpBean = sdOtpService.getValidOtp(userId, SdOtpEntity.ACTION.INIT_PWD);
            if (sdOtpBean != null) {
                throw new InvalidOtpException("Please check your email. OTP is already sent within 15 min.");
            }

            // generate init password otp
            String otp = sdOtpService.generateOtp(userId, SdOtpEntity.ACTION.INIT_PWD);
            LOG.info("Generated password OTP successfully for user " + username + ".");
            String recipient = sdUserEntity.getEmail();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(SdEmailBean.EMAIL_BASIC_RECIPIENT_NAME, sdUserEntity.getName());
            dataMap.put(SdEmailBean.INIT_PW_EMAIL.OTP, otp);

            // send otp email
            sdEmailService.send(SdEmailBean.INIT_PW_EMAIL.TEMPLATE_ID, recipient, dataMap);
        } else {
            // valid reset password otp
            SdOtpBean sdOtpBean = sdOtpService.getValidOtp(userId, SdOtpEntity.ACTION.RESET_PWD);
            if (sdOtpBean != null) {
                throw new InvalidOtpException("Please check your email. OTP is already sent within 15 min.");
            }

            // generate reset password otp
            String otp = sdOtpService.generateOtp(userId, SdOtpEntity.ACTION.RESET_PWD);
            LOG.info("Generated password OTP successfully for user " + username + ".");
            String recipient = sdUserEntity.getEmail();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(SdEmailBean.EMAIL_BASIC_RECIPIENT_NAME, sdUserEntity.getName());
            dataMap.put(SdEmailBean.RESET_PW_EMAIL.OTP, otp);

            // send otp email
            sdEmailService.send(SdEmailBean.RESET_PW_EMAIL.TEMPLATE_ID, recipient, dataMap);
        }
    }
}
