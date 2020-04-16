package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.impl.BtuUserServiceImpl;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.sd.core.dao.entity.SdOtpEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import com.hkt.btu.sd.core.dao.mapper.SdUserMapper;
import com.hkt.btu.sd.core.dao.mapper.SdUserRoleMapper;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.SdEmailService;
import com.hkt.btu.sd.core.service.SdOtpService;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.*;
import com.hkt.btu.sd.core.service.populator.SdUserBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdUserRoleBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
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

    @Resource(name = "userRoleService")
    SdUserRoleService userRoleService;

    @Resource(name = "userRoleBeanPopulator")
    SdUserRoleBeanPopulator sdUserRoleBeanPopulator;

    @Resource
    SdUserRoleMapper sdUserRoleMapper;

    @Override
    public BtuUserBean getCurrentUserBean() throws UserNotFoundException {
        BtuUser btuUser = this.getCurrentUser();
        if (btuUser == null) {
            throw new UserNotFoundException("No UserBean found in security context!");
        } else if (btuUser.getUserBean()==null) {
            throw new UserNotFoundException("No BtuUserBean found in security context!");
        } else if (btuUser.getUserBean() instanceof SdUserBean) {
            return btuUser.getUserBean();
        } else {
            LOG.debug("Found BtuUserBean in security context.");
            return btuUser.getUserBean();
        }
    }


    @Override
    public BtuUserBean getUserBeanByUsername(String username) {
        // get user data
        SdUserEntity sdUserEntity = sdUserMapper.getLdapUserByUserId(username);
        if (sdUserEntity == null) {
            return null;
        }

        // get user role data
        List<SdUserRoleEntity> userRoleEntityList = sdUserRoleMapper.getUserRoleByUserIdAndStatus(sdUserEntity.getUserId(), SdUserRoleBean.ACTIVE_ROLE_STATUS);
        if (CollectionUtils.isEmpty(userRoleEntityList)) {
            return null;
        }
        List<SdUserRoleBean> userRole = userRoleEntityList.stream().map(role -> {
            SdUserRoleBean bean = new SdUserRoleBean();
            sdUserRoleBeanPopulator.populate(role, bean);
            return bean;
        }).collect(Collectors.toList());

        List<SdUserRoleBean> results = new LinkedList<>();

        for (SdUserRoleBean role : userRole) {
            results.add(role);
            if (StringUtils.isNotEmpty(role.getParentRoleId())) {
                List<SdUserRoleBean> parentRoleByRoleId = userRoleService.getParentRoleByRoleId(role.getParentRoleId());
                if (CollectionUtils.isNotEmpty(parentRoleByRoleId)) {
                    results.addAll(parentRoleByRoleId);
                }
            }
        }

        // construct bean
        SdUserBean userBean = new SdUserBean();
        sdUserBeanPopulator.populate(sdUserEntity, userBean);
        // set user role to userbean.
        if (CollectionUtils.isNotEmpty(results)) {
            Set<GrantedAuthority> grantedAuthSet = results.stream().map(
                    role -> new SimpleGrantedAuthority(role.getRoleId())).collect(Collectors.toSet());
            userBean.setAuthorities(grantedAuthSet);
        }

        return userBean;
    }

    @Override
    public String getCurrentUserUserId() {
        SdUserBean sdUserBean = (SdUserBean) getCurrentUserBean();
        return sdUserBean.getUserId();
    }

    @Override
    public SdUserBean getCurrentSdUserBean() throws UserNotFoundException {
        BtuUserBean userBean = getCurrentUserBean();
        if (userBean instanceof SdUserBean) {
            return (SdUserBean) userBean;
        }
        return null;
    }

    @Override
    public SdUserBean getUserByUserId(String userId) throws UserNotFoundException, InsufficientAuthorityException {
        if (userId == null) {
            throw new UserNotFoundException("Empty user id input.");
        }

        Set<GrantedAuthority> authorities = getCurrentUserBean().getAuthorities();

        // get user data
        SdUserEntity sdUserEntity = sdUserMapper.getUserByUserId(userId);
        if (sdUserEntity == null) {
            throw new UserNotFoundException("Cannot find user with id " + userId + ".");
        }

        // get user group data
        List<SdUserRoleEntity> roleEntityList = sdUserRoleMapper.getUserRoleByUserIdAndStatus(userId, SdUserRoleBean.ACTIVE_ROLE_STATUS);

        // get roleId
        List<String> roleIdList = roleEntityList.stream().map(SdUserRoleEntity::getRoleId).collect(Collectors.toList());

        userRoleService.checkUserRole(authorities, roleIdList);

        // construct bean
        SdUserBean userBean = new SdUserBean();
        sdUserBeanPopulator.populate(sdUserEntity, userBean);

        sdUserBeanPopulator.populate(roleEntityList, userBean);

        return userBean;
    }


    @Transactional
    public String createLdapUser(String name, String mobile, String employeeNumber,
                                 String ldapDomain, String email, String primaryRoleId, List<String> toGrantRoleIdList)
            throws UserNotFoundException, InvalidInputException {
        SdUserEntity existUser = sdUserMapper.getLdapUserByUserId(employeeNumber);
        if (existUser != null) {
            throw new DuplicateUserEmailException("The user already exists.");
        }

        // get current userId for CreateBy
        String createBy = getCurrentUserUserId();
        // check current user has right to create user of user role
        boolean isEligibleUserGroup = userRoleService.isEligibleToGrantUserRole(toGrantRoleIdList);
        if (!isEligibleUserGroup) {
            LOG.warn("Ineligible to create user of selected user role (" + toGrantRoleIdList + ") by user (" + createBy + ").");
            throw new InvalidInputException("Invalid user role.");
        }

        // Data input
        SdUserEntity userEntity = new SdUserEntity();
        userEntity.setName(name);
        userEntity.setCreateby(createBy);
        userEntity.setUserId(employeeNumber);
        userEntity.setEmail(email);
        userEntity.setMobile(mobile);
        userEntity.setStatus(SdUserEntity.STATUS.ACTIVE);
        userEntity.setLdapDomain(ldapDomain);
        userEntity.setPrimaryRoleId(primaryRoleId);

        // create user in db
        sdUserMapper.insertUser(userEntity);

        // create user role relation in db
        if (!CollectionUtils.isEmpty(toGrantRoleIdList)) {
            sdUserRoleMapper.insertUserUserRole(employeeNumber, toGrantRoleIdList);
        }

        LOG.info("User (id: " + employeeNumber + ") created.");
        return employeeNumber;
    }

    @Override
    public SdCreateResultBean createUser(String userId, String name, String mobile, String email, String primaryRoleId, List<String> roleIdList)
            throws UserNotFoundException {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidInputException("Empty user name.");
        }

        if (StringUtils.isNotEmpty(userId)) {
            SdUserEntity user = sdUserMapper.getLdapUserByUserId(userId);
            if (user != null) {
                throw new DuplicateUserEmailException("Duplicate username.");
            }
        }

        // get current user user id
        String createby = getCurrentUserUserId();

        // check current user has right to create user of user role
        boolean isEligibleUserGroup = userRoleService.isEligibleToGrantUserRole(roleIdList);
        if (!isEligibleUserGroup) {
            LOG.warn("Ineligible to create user of selected user role (" + roleIdList + ") by user (" + createby + ").");
            throw new InvalidInputException("Invalid user role.");
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
        sdUserEntity.setPrimaryRoleId(primaryRoleId);

        // create user in db
        sdUserMapper.insertUser(sdUserEntity);

        // create user group relation in db
        if (!CollectionUtils.isEmpty(roleIdList)) {
            sdUserRoleMapper.insertUserUserRole(userId, roleIdList);
        }

        LOG.info("User (id: " + userId + ") " + email + " created.");


        // send init password email
        try {
            requestResetPassword(userId);
        } catch (UserNotFoundException e) {
            LOG.warn("User not found (" + email + ").");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return SdCreateResultBean.of(userId, null);
    }


    @Override
    @Transactional
    public void updateUser(String userId, String newName, String newMobile, String email, String primaryRoleId, List<String> userRoleIdList)
            throws UserNotFoundException, InsufficientAuthorityException {
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

        sdUserMapper.updateUser(userId, name, encryptedMobile, email, primaryRoleId, modifier.getUserId());

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
        if (StringUtils.isEmpty(userDetailBean.getDomainEmail()) &&
                StringUtils.isNotEmpty(userDetailBean.getLdapDomain())) {
            BtuUserBean btuUserBean = super.verifyLdapUser(user, userDetailBean);
            String userId = userDetailBean.getUserId();
            String email = btuUserBean.getEmail();
            String username = btuUserBean.getUsername();
            if (StringUtils.isNotEmpty(email)) {
                sdUserMapper.updateLdapUser(userId, null, email, SdUserEntity.SYSTEM.USER_ID);
            }
            if (StringUtils.isNotEmpty(username)) {
                sdUserMapper.updateLdapUser(userId, username, null, SdUserEntity.SYSTEM.USER_ID);
            }
        }
        return null;
    }

    @Override
    public void updateLdapInfo(String userId, String username, String ldapEmail) {
        sdUserMapper.updateLdapUser(userId, username, ldapEmail,SdUserEntity.SYSTEM.USER_ID);
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

    public boolean isActive(BtuUserBean btuUserBean) {
        if (ObjectUtils.isEmpty(btuUserBean)) {
            return false;
        }
        return SdUserEntity.STATUS.ACTIVE.equals(btuUserBean.getStatus());
    }

    private void checkValidPassword(String plaintext) throws InvalidPasswordException {
        final int MIN_LENGTH = 12;
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

        LOG.info(String.format(
                "Searching user with {userId: %s, email: %s, name: %s}",
                userId, email, name));

        Integer totalCount = sdUserMapper.countSearchUser(userId, email, name);

        List<SdUserEntity> sdUserEntityList = sdUserMapper.searchUser(offset, pageSize, userId, email, name);

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


    @Override
    @Transactional
    public String changeUserTypeToPCCWOrHktUser(String oldUserId, String name, String mobile, String employeeNumber, String email)
            throws InvalidInputException, UserNotFoundException {
        // Determine if it is already a PCCW/HKT user. UserId starts with T
        if (oldUserId.contains(SdUserBean.CREATE_USER_PREFIX.PCCW_HKT_USER)) {
            throw new InvalidInputException("You are already PCCW/HKT user.");
        }
        SdUserBean currentUserBean = (SdUserBean) getCurrentUserBean();

        SdUserEntity user = sdUserMapper.getLdapUserByUserId(oldUserId);

        checkDuplicateUser(oldUserId, currentUserBean, user);

        SdUserEntity userByEmail = sdUserMapper.getUserByEmail(email);

        // Check User Email
        checkUserEmailDuplicate(user.getEmail(), userByEmail.getEmail());

        List<SdUserRoleEntity> userRoleByUserId = sdUserRoleMapper.getUserRoleByUserId(oldUserId);

        sdUserMapper.changeUserType(oldUserId, name, mobile, employeeNumber, null, email, currentUserBean.getUserId());
        userRoleService.changeUserIdInUserUserRole(oldUserId, employeeNumber, userRoleByUserId);

        return employeeNumber;
    }

    @Override
    @Transactional
    public String changeUserTypeToNonPCCWOrHktUser(String oldUserId, String name, String mobile, String employeeNumber, String email) throws InvalidInputException, UserNotFoundException {
        // Determine if it is already a PCCW/HKT user. UserId starts with T
        if (oldUserId.contains(SdUserBean.CREATE_USER_PREFIX.NON_PCCW_HKT_USER)) {
            throw new InvalidInputException("You are already PCCW/HKT user.");
        }
        SdUserBean currentUserBean = (SdUserBean) getCurrentUserBean();
        SdUserEntity user = sdUserMapper.getLdapUserByUserId(oldUserId);

        checkDuplicateUser(oldUserId, currentUserBean, user);

        SdUserEntity userByEmail = sdUserMapper.getUserByEmail(email);

        // Check User Email
        if (StringUtils.isNotEmpty(email)) {
            checkUserEmailDuplicate(user.getEmail(), userByEmail.getEmail());
        }

        List<SdUserRoleEntity> userRoleByUserId = sdUserRoleMapper.getUserRoleByUserId(oldUserId);

        sdUserMapper.changeUserType(oldUserId, name, mobile, employeeNumber, null, email, currentUserBean.getUserId());

        // Change USER_ID IN USER_USER_ROLE
        userRoleService.changeUserIdInUserUserRole(oldUserId, employeeNumber, userRoleByUserId);

        return employeeNumber;
    }

    @Override
    @Transactional
    public String changeUserTypeToLdapUser(String oldUserId, String name, String mobile,
                                           String employeeNumber, String ldapDomain, String email)
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
        sdUserMapper.changeUserType(oldUserId, name, mobile, employeeNumber, ldapDomain, email, currentUserBean.getUserId());

        // Change USER_ID IN USER_USER_ROLE
        userRoleService.changeUserIdInUserUserRole(oldUserId, employeeNumber, userRoleByUserId);

        LOG.info("User :" + oldUserId + "has been change to LDAP user =>" + employeeNumber);

        return employeeNumber;
    }

    public void requestResetPassword(String username) throws UserNotFoundException, MessagingException {
        // check requestor rights
        BtuUserBean currentUser = getCurrentUserBean();
        // get user data
        SdUserBean resetPwdUser = getUserByUserId(username);
        if (ObjectUtils.isEmpty(resetPwdUser)) {
            throw new UserNotFoundException();
        }
        if (currentUser.getRoles().stream().noneMatch(s -> StringUtils.equals(s, SdUserRoleEntity.SYS_ADMIN))) {
            if (resetPwdUser.getRoles().stream().noneMatch(s -> currentUser.getRoles().contains("TH__" + s))) {
                throw new RuntimeException("Reset password error: You are not the team head of this user.");
            }
        }
        String userId = resetPwdUser.getUserId();
        // reject LDAP user to reset password
        String ldapDomain = resetPwdUser.getLdapDomain();
        if (StringUtils.isNotEmpty(ldapDomain)) {
            throw new InvalidUserTypeException("LDAP users are not allowed to reset passwords.");
        }

        boolean isNewlyCreated = resetPwdUser.getPasswordModifydate() == null;
        String recipient = Optional.ofNullable(currentUser.getEmail()).orElseThrow(() -> new UserNotFoundException("Current User not email."));
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(SdEmailBean.EMAIL_BASIC_RECIPIENT_NAME, currentUser.getName());
        if (isNewlyCreated) {
            // valid init password otp
            SdOtpBean sdOtpBean = sdOtpService.getValidOtp(userId, SdOtpEntity.ACTION.INIT_PWD);
            if (sdOtpBean != null) {
                throw new InvalidOtpException("Please check your email. OTP is already sent within 15 min.");
            }

            // generate init password otp
            String otp = sdOtpService.generateOtp(userId, SdOtpEntity.ACTION.INIT_PWD);
            LOG.info("Generated password OTP successfully for user " + username + ".");
            dataMap.put(SdEmailBean.INIT_PW_EMAIL.OTP, otp);
            dataMap.put(SdEmailBean.INIT_PW_EMAIL.NAME_OF_NEW_USER, resetPwdUser.getName());

            // send otp email
            sdEmailService.send(SdEmailBean.INIT_PW_EMAIL.TEMPLATE_ID, recipient, null, dataMap);
        } else {
            // valid reset password otp
            SdOtpBean sdOtpBean = sdOtpService.getValidOtp(userId, SdOtpEntity.ACTION.RESET_PWD);
            if (sdOtpBean != null) {
                throw new InvalidOtpException("Please check your email. OTP is already sent within 15 min.");
            }

            // generate reset password otp
            String otp = sdOtpService.generateOtp(userId, SdOtpEntity.ACTION.RESET_PWD);
            LOG.info("Generated password OTP successfully for user " + username + ".");
            dataMap.put(SdEmailBean.RESET_PW_EMAIL.OTP, otp);
            dataMap.put(SdEmailBean.RESET_PW_EMAIL.NAME_OF_USER, resetPwdUser.getName());

            // send otp email
            sdEmailService.send(SdEmailBean.RESET_PW_EMAIL.TEMPLATE_ID, recipient, null, dataMap);
        }
    }


    @Override
    public String getUserLogonPage() {
        boolean hasOperatorRole = userRoleService.hasUserRole(SdUserRoleBean.ROLE_ID.OPERATOR);

        if (hasOperatorRole) {
            return SdUserRolePathCtrlBean.OPERATOR_LOGON_PATH;
        } else {
            return SdUserRolePathCtrlBean.DEFAULT_LOGON_PATH;
        }
    }

    private void checkUserEmailDuplicate(String oldUserEmail, String queryEmail) {
        if (StringUtils.isNotEmpty(oldUserEmail) && StringUtils.isNotEmpty(queryEmail)) {
            if (!queryEmail.equals(oldUserEmail)) {
                throw new InvalidInputException("Email Already Exists.");
            }
        }
    }

    private void checkDuplicateUser(String oldUserId, SdUserBean currentUserBean, SdUserEntity user) {
        if (currentUserBean.getUserId().equals(oldUserId)) {
            throw new InvalidInputException("Cannot change you own account.");
        }

        if (user == null) {
            throw new UserNotFoundException("User not Exist.");
        }
    }

    @Override
    public Page<SdUserBean> getTeamHeadUser(Pageable pageable, String teamHead)
            throws AuthorityNotFoundException {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        LOG.info(String.format("Searching user with {teamHead: %s}", teamHead));

        Integer totalCount = sdUserMapper.countTeamHeadUser(teamHead, SdUserRoleBean.ROLE_ID.TEAM_HEAD);

        List<SdUserEntity> sdUserEntityList = sdUserMapper.getTeamHeadUser(offset, pageSize, teamHead, SdUserRoleBean.ROLE_ID.TEAM_HEAD);

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cleanOutDatePwdHistData() {
        sdUserMapper.deleteOutDatePwdHistData();
    }

    @Override
    public List<BtuUserBean> getApiUser() {
        List<SdUserEntity> sdUserEntityList = sdUserMapper.getApiUser();
        List<BtuUserBean> btuUserBeanList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(sdUserEntityList)) {
            for (SdUserEntity sdUserEntity : sdUserEntityList) {
                SdUserBean sdUserBean = new SdUserBean();
                sdUserBeanPopulator.populate(sdUserEntity, sdUserBean);
                btuUserBeanList.add(sdUserBean);
            }
        }
        return btuUserBeanList;
    }
}
