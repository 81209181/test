package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.impl.BtuUserServiceImpl;
import com.hkt.btu.common.spring.security.core.userdetails.BtuUser;
import com.hkt.btu.noc.core.dao.entity.NocUserEntity;
import com.hkt.btu.noc.core.dao.entity.NocUserGroupEntity;
import com.hkt.btu.noc.core.dao.mapper.NocUserGroupMapper;
import com.hkt.btu.noc.core.dao.mapper.NocUserMapper;
import com.hkt.btu.noc.core.exception.*;
import com.hkt.btu.noc.core.service.*;
import com.hkt.btu.noc.core.service.bean.NocCompanyBean;
import com.hkt.btu.noc.core.service.bean.NocEmailBean;
import com.hkt.btu.noc.core.service.bean.NocOtpBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;
import com.hkt.btu.noc.core.service.populator.NocUserBeanPopulator;
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

public class NocUserServiceImpl extends BtuUserServiceImpl implements NocUserService {
    private static final Logger LOG = LogManager.getLogger(NocUserServiceImpl.class);

    @Resource
    NocUserMapper nocUserMapper;
    @Resource
    NocUserGroupMapper nocUserGroupMapper;

    @Resource(name = "userBeanPopulator")
    NocUserBeanPopulator nocUserBeanPopulator;

    @Resource(name = "userGroupService")
    NocUserGroupService nocUserGroupService;
    @Resource(name = "companyService")
    NocCompanyService nocCompanyService;
    @Resource(name = "otpService")
    NocOtpService nocOtpService;
    @Resource(name = "emailService")
    NocEmailService nocEmailService;

    @Resource(name = "sensitiveDataService")
    NocSensitiveDataService nocSensitiveDataService;

    @Override
    public BtuUserBean getCurrentUserBean() throws UserNotFoundException {
        BtuUser btuUser = this.getCurrentUser();
        if (btuUser==null){
            throw new UserNotFoundException("No UserBean found in security context!");
        }else if (btuUser.getUserBean() instanceof NocUserBean) {
            return btuUser.getUserBean();
        }
        throw new UserNotFoundException("No NocUserBean found in security context!");
    }


    @Override
    public BtuUserBean getUserBeanByUsername(String username) {
        // make email lower case
        String email = StringUtils.lowerCase(username);

        // get user data
        NocUserEntity nocUserEntity = nocUserMapper.getUserByEmail(email);
        if (nocUserEntity==null) {
            return null;
        }

        // get user group data
        List<NocUserGroupEntity> groupEntityList = nocUserGroupMapper.getUserGroupByUserId(nocUserEntity.getUserId());

        // construct bean
        NocUserBean userBean = new NocUserBean();
        nocUserBeanPopulator.populate(nocUserEntity, userBean);
        nocUserBeanPopulator.populate(groupEntityList, userBean);

        return userBean;
    }

    @Override
    public Integer getCurrentUserUserId() {
        NocUserBean nocUserBean = (NocUserBean) getCurrentUserBean();
        return nocUserBean.getUserId();
    }

    @Override
    public NocUserBean getUserByUserId(Integer userId) throws UserNotFoundException {
        if(userId==null){
            throw new UserNotFoundException("Empty user id input.");
        }

        // determine company id restriction
        Integer companyId = getCompanyIdRestriction();

        // get user data
        NocUserEntity nocUserEntity = nocUserMapper.getUserByUserId(userId, companyId);
        if (nocUserEntity==null) {
            throw new UserNotFoundException("Cannot find user with id " + userId + ".");
        }

        // get user group data
        List<NocUserGroupEntity> groupEntityList = nocUserGroupMapper.getUserGroupByUserId(userId);

        // construct bean
        NocUserBean userBean = new NocUserBean();
        nocUserBeanPopulator.populate(nocUserEntity, userBean);
        nocUserBeanPopulator.populate(groupEntityList, userBean);

        return userBean;
    }


    @Transactional
    public Integer createUser(String name, String mobile, String email, String staffId,
                              Integer companyId, List<String> groupIdList)
            throws DuplicateUserEmailException, UserNotFoundException, GeneralSecurityException {
        if(StringUtils.isEmpty(name)){
            throw new InvalidInputException("Empty user name.");
        } else if (StringUtils.isEmpty(email)) {
            throw new InvalidInputException("Empty email.");
        }

        // get current user user id
        Integer createby = getCurrentUserUserId();

        // check current user has right to create user of company
        boolean isEligibleCompany = isEligibleCompany(companyId);
        if( ! isEligibleCompany ){
            LOG.warn("Ineligible to create user of selected company (" + companyId + ") by user (" + createby + ").");
            throw new InvalidInputException("Invalid company ID.");
        }


        // check current user has right to create user of user group
        boolean isEligibleUserGroup = nocUserGroupService.isEligibleToGrantUserGroup(groupIdList);
        if( ! isEligibleUserGroup ){
            LOG.warn("Ineligible to create user of selected user group (" + groupIdList + ") by user (" + createby + ").");
            throw new InvalidInputException("Invalid user group.");
        }


        // check email duplicated
        NocUserEntity userEntity = nocUserMapper.getUserByEmail(email);
        if (userEntity!=null){
            throw new DuplicateUserEmailException();
        }

        // make email lower case (**assume email are all lower case)
        email = StringUtils.lowerCase(email);

        // generate dummy password
        UUID uuid = UUID.randomUUID();
        String password = uuid.toString();
        String encodedPassword = encodePassword( password );

        // encrypt
        byte[] encryptedMobile = StringUtils.isEmpty(mobile) ? null : nocSensitiveDataService.encryptFromString(mobile);
        byte[] encryptedStaffId = StringUtils.isEmpty(staffId) ? null : nocSensitiveDataService.encryptFromString(staffId);

        // prepare entity
        NocUserEntity nocUserEntity = new NocUserEntity();
        nocUserEntity.setName(name);
        nocUserEntity.setStatus(NocUserEntity.STATUS.ACTIVE);
        nocUserEntity.setMobile(encryptedMobile);
        nocUserEntity.setEmail(email);
        nocUserEntity.setCompanyId(companyId);
        nocUserEntity.setStaffId(encryptedStaffId);
        nocUserEntity.setPassword(encodedPassword);
        nocUserEntity.setCreateby(createby);

        // create user in db
        nocUserMapper.insertUser(nocUserEntity);

        // get new user id
        Integer newUserId = nocUserEntity.getUserId();

        // create user group relation in db
        if ( ! CollectionUtils.isEmpty(groupIdList) ) {
            for(String groupId : groupIdList){
                nocUserGroupMapper.insertUserUserGroup(newUserId, groupId, createby);
            }
        }

        LOG.info("User (id: " + newUserId + ") " + email + " created.");

        // send init password email
        try {
            requestResetPassword(email);
        } catch (UserNotFoundException e){
            LOG.warn("User not found (" + email + ").");
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
        }

        return newUserId;
    }

    @Override
    @Transactional
    public void updateUser(Integer userId, String newName, String newMobile, String newStaffId,
                           Boolean isNewAdmin, Boolean isNewUser, Boolean isNewCAdmin, Boolean isNewCUser)
            throws UserNotFoundException, InsufficientAuthorityException, InvalidInputException, GeneralSecurityException {
        NocUserBean currentUser = (NocUserBean) this.getCurrentUserBean();
        if(currentUser.getUserId().equals(userId)){
            throw new InvalidInputException ("Cannot update your own account!");
        }

        // get modifier
        NocUserBean modifier = (NocUserBean) getCurrentUserBean();

        // get current data
        NocUserBean targetUserBean = getUserByUserId(userId);
        Set<GrantedAuthority> authorities = targetUserBean.getAuthorities();
        Boolean isTargetAdmin = hasAnyAuthority(authorities, NocUserGroupEntity.GROUP_ID.ADMIN);
        Boolean isTargetUser = hasAnyAuthority(authorities, NocUserGroupEntity.GROUP_ID.USER);
        Boolean isTargetCAdmin = hasAnyAuthority(authorities, NocUserGroupEntity.GROUP_ID.C_ADMIN);
        Boolean isTargetCUser = hasAnyAuthority(authorities, NocUserGroupEntity.GROUP_ID.C_USER);

        // list to-be-update data
        String name = StringUtils.equals(newName, targetUserBean.getName()) ? null : newName;
        String mobile = StringUtils.equals(newMobile, targetUserBean.getMobile()) ? null : newMobile;
        String staffId = StringUtils.equals(newStaffId, targetUserBean.getStaffId()) ? null : newStaffId;

        Boolean updateIsAdmin = isTargetAdmin==isNewAdmin ? null : isNewAdmin;
        Boolean updateIsUser = isTargetUser==isNewUser ? null : isNewUser;
        Boolean updateIsCAdmin = isTargetCAdmin==isNewCAdmin ? null : isNewCAdmin;
        Boolean updateIsCUser = isTargetCUser==isNewCUser ? null : isNewCUser;

        // encrypt
        byte[] encryptedMobile = StringUtils.isEmpty(mobile) ? null : nocSensitiveDataService.encryptFromString(mobile);
        byte[] encryptedStaffId = StringUtils.isEmpty(staffId) ? null : nocSensitiveDataService.encryptFromString(staffId);

        // update user
        nocUserMapper.updateUser(userId, name, encryptedMobile, encryptedStaffId, modifier.getUserId());

        // update user group
        nocUserGroupService.updateUserGroup(userId, updateIsAdmin, updateIsUser, updateIsCAdmin, updateIsCUser, modifier);

        LOG.info( String.format("Updated user. [name:%b, mobile:%b, staffId:%b]", name!=null, mobile!=null, staffId!=null) );
    }

    @Override
    public List<NocCompanyBean> getEligibleCompanyList() {
        List<NocCompanyBean> result;

        // determine company id restriction
        Integer companyIdRestriction = getCompanyIdRestriction();


        if(companyIdRestriction==null){
            result = nocCompanyService.getAllCompany();
        }else {
            NocCompanyBean nocCompanyBean = nocCompanyService.getCompanyById(companyIdRestriction);
            result = new LinkedList<>();
            result.add(nocCompanyBean);
        }

        return result;
    }

    private boolean isEligibleCompany(Integer companyId){
        List<NocCompanyBean> eligibleCompanyList = getEligibleCompanyList();
        if(CollectionUtils.isEmpty(eligibleCompanyList)){
            return false;
        }

        for(NocCompanyBean nocCompanyBean : eligibleCompanyList){
            if(companyId.equals(nocCompanyBean.getCompanyId())){
                return true;
            }
        }

        return false;
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
        NocUserEntity nocUserEntity = nocUserMapper.getUserByUserId(userId, null);
        if(nocUserEntity==null){
            throw new UserNotFoundException("User not found in database (" + userId + ").");
        }
        String encodedOldPwd = nocUserEntity.getPassword();

        // record old password
        LOG.info("Inserting current password to history...");
        nocUserMapper.insertPasswordHist(userId, encodedOldPwd);

        // check password history
        LOG.info("Checking conflict with password history...");
        List<String> passwordHistoryList = nocUserMapper.getPasswordHistByUserId(userId);
        if(!CollectionUtils.isEmpty(passwordHistoryList)){
            for(String encodedPastPassword : passwordHistoryList){
                if(matchPassword(rawNewPassword, encodedPastPassword)){
                    throw new InvalidPasswordException("Conflict with password history.");
                }
            }
        }

        // update password
        LOG.info("Updating password...");
        String encodedNewPwd = encodePassword(rawNewPassword);
        nocUserMapper.updateUserPassword(userId, encodedNewPwd);
    }

    @Override
    @Transactional
    public void updateUserPwd(Integer userId, String rawOldPassword, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException {
        NocUserEntity nocUserEntity = nocUserMapper.getUserByUserId(userId, null);
        if(nocUserEntity==null){
            throw new UserNotFoundException("User not found in database (" + userId + ").");
        }

        // check old password
        LOG.info("Checking input matches with old password...");
        String encodedOldPwd = nocUserEntity.getPassword();
        if(! matchPassword(rawOldPassword, encodedOldPwd)){
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
        NocOtpBean nocOtpBean = nocOtpService.getValidResetPwdOtp(otp);
        if(nocOtpBean==null){
            throw new InvalidInputException("Invalid OTP.");
        }

        Integer userId = nocOtpBean.getUserId();
        LOG.info("Checked password reset OTP for user (" + userId + ").");

        // update new password
        updatePassword(userId, rawNewPassword);

        // expire otp
        nocOtpService.expireOtp(otp);
    }


    @Transactional
    public void activateUserByUsername(String username) throws UserNotFoundException, InvalidInputException {
        NocUserBean currentUser = (NocUserBean) this.getCurrentUserBean();
        if( StringUtils.equals(currentUser.getEmail(), username) ){
            throw new InvalidInputException ("Cannot activate your own account!");
        }

        Integer modifyby = getCurrentUserUserId();
        nocUserMapper.resetLoginTriedByUsername(username);
        nocUserMapper.updateUserStatusByUsername(username, NocUserEntity.STATUS.ACTIVE, modifyby);
    }

    public void resetLoginTriedByUsername(String username) {
        nocUserMapper.resetLoginTriedByUsername(username);
    }

    public void addLoginTriedByUsername(String username) {
        nocUserMapper.addLoginTriedByUsername(username);
    }

    public void lockUserByUsername(String username) {
        Integer modifyby = NocUserEntity.SYSTEM.USER_ID;
        nocUserMapper.updateUserStatusByUsername(username, NocUserEntity.STATUS.LOCKED, modifyby);
    }

    public void disableUserByUsername(String username) throws UserNotFoundException, InvalidInputException{
        NocUserBean currentUser = (NocUserBean) this.getCurrentUserBean();
        if( StringUtils.equals(currentUser.getEmail(), username) ){
            throw new InvalidInputException ("Cannot deactivate your own account!");
        }

        Integer modifyby = getCurrentUserUserId();
        nocUserMapper.updateUserStatusByUsername(username, NocUserEntity.STATUS.DISABLE, modifyby);
    }

    public boolean isEnabled(BtuUserBean btuUserBean) {
        if (btuUserBean==null){
            return false;
        }
        return ! NocUserEntity.STATUS.DISABLE.equals(btuUserBean.getStatus());
    }

    public boolean isNonLocked(BtuUserBean btuUserBean) {
        if (btuUserBean==null){
            return false;
        }
        return ! NocUserEntity.STATUS.LOCKED.equals(btuUserBean.getStatus());
    }

    private void checkValidPassword(String plaintext) throws InvalidPasswordException{
        final int MIN_LENGTH = 8;
        final int MAX_LENGTH = 20;

        // check length
        if(StringUtils.isEmpty(plaintext)){
            throw new InvalidPasswordException("Empty password.");
        } else if (plaintext.length()<MIN_LENGTH){
            throw new InvalidPasswordException("Password should be at least " + MIN_LENGTH + " char long.");
        } else if (plaintext.length()>MAX_LENGTH){
            throw new InvalidPasswordException("Password should be at most " + MAX_LENGTH + " char long.");
        }

        final String REGEX_NUM   = ".*[0-9].*";
        final String REGEX_ALPHA = ".*[a-zA-Z].*";

        // check pattern
        boolean hasNum = plaintext.matches(REGEX_NUM);
        boolean hasAlpha = plaintext.matches(REGEX_ALPHA);
        if(!hasNum || !hasAlpha){
            throw new InvalidPasswordException("Password must contain both numeric and alphabetic characters.");
        }
    }

    public Page<NocUserBean> searchUser(Pageable pageable, Integer userId, String email, String name, String userGroupId)
            throws AuthorityNotFoundException{
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        // make email lower case (**assume email are all lower case)
        email = StringUtils.lowerCase(email);

        // determine company id restriction
        Integer companyId = getCompanyIdRestriction();

        LOG.info( String.format(
                "Searching user with {userId: %s, email: %s, name: %s, companyId: %s, userGroupId: %s}",
                userId, email, name, companyId, userGroupId) );

        // get total count
        Integer totalCount = nocUserMapper.countSearchUser(companyId, userId, email, name, userGroupId);

        // get content
        List<NocUserEntity> nocUserEntityList = nocUserMapper.searchUser(offset, pageSize, companyId, userId, email, name, userGroupId);
        List<NocUserBean> nocUserBeanList = new LinkedList<>();
        if(! CollectionUtils.isEmpty(nocUserEntityList)){
            for (NocUserEntity nocUserEntity : nocUserEntityList){
                NocUserBean nocUserBean = new NocUserBean();
                nocUserBeanPopulator.populate(nocUserEntity, nocUserBean);
                nocUserBeanList.add(nocUserBean);
            }
        }

        return new PageImpl<>(nocUserBeanList, pageable, totalCount);
    }

    /**
     * Return company id that the current user belongs and has access to,
     * return null if there is no limitation.
     */
    public Integer getCompanyIdRestriction() throws AuthorityNotFoundException {
        NocUserBean currentUser;
        try {
            currentUser = (NocUserBean) getCurrentUserBean();
        }catch (UserNotFoundException e){
            throw new AuthorityNotFoundException(e.getMessage());
        }

        Integer companyIdRestriction = currentUser.getCompanyId();
        if(isInternalUser()){
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
        NocUserBean currentUser;
        try {
            currentUser = (NocUserBean) getCurrentUserBean();
        }catch (UserNotFoundException e){
            throw new AuthorityNotFoundException(e.getMessage());
        }

        Integer userIdRestriction = currentUser.getUserId();
        if(isAdminUser()){
            // admin user has no restriction over user id (independent of company id)
            return null;
        }

        return userIdRestriction;
    }

    @Override
    public boolean isInternalUser() {
        return hasAnyAuthority(NocUserGroupEntity.GROUP_ID.ROOT, NocUserGroupEntity.GROUP_ID.ADMIN, NocUserGroupEntity.GROUP_ID.USER);
    }

    @Override
    public boolean isAdminUser() {
        return hasAnyAuthority(NocUserGroupEntity.GROUP_ID.ROOT, NocUserGroupEntity.GROUP_ID.ADMIN, NocUserGroupEntity.GROUP_ID.C_ADMIN);
    }


    public void requestResetPassword(String username) throws UserNotFoundException, MessagingException {
        NocUserEntity nocUserEntity = nocUserMapper.getUserByEmail(username);
        if(nocUserEntity==null){
            throw new UserNotFoundException();
        }

        Integer userId = nocUserEntity.getUserId();
        String otp = nocOtpService.generatePwdResetOtp(userId);
        boolean isNewlyCreated = nocUserEntity.getPasswordModifydate()==null;
        LOG.info("Generated password OTP successfully for user " + username + ".");


        // send otp email
        if(isNewlyCreated) {
            String recipient = nocUserEntity.getEmail();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(NocEmailBean.EMAIL_BASIC_RECIPIENT_NAME, nocUserEntity.getName());
            dataMap.put(NocEmailBean.INIT_PW_EMAIL.OTP, otp);

            nocEmailService.send(NocEmailBean.INIT_PW_EMAIL.TEMPLATE_ID, recipient, dataMap);
        } else {
            String recipient = nocUserEntity.getEmail();

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(NocEmailBean.EMAIL_BASIC_RECIPIENT_NAME, nocUserEntity.getName());
            dataMap.put(NocEmailBean.RESET_PW_EMAIL.OTP, otp);

            nocEmailService.send(NocEmailBean.RESET_PW_EMAIL.TEMPLATE_ID, recipient, dataMap);
        }
    }
}
