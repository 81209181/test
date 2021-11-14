package com.hkt.btu.sd.core.service;


import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.sd.core.exception.InsufficientAuthorityException;
import com.hkt.btu.sd.core.exception.InvalidPasswordException;
import com.hkt.btu.sd.core.service.bean.SdCreateResultBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface SdUserService extends BtuUserService {

    SdCreateResultBean createUser(String userId, String employeeNumber, String name, String mobile, String email, String primaryRoleId, List<String> toGrantRoleIdList)
            throws UserNotFoundException;
    String createLdapUser(String userId, String mobile, String employeeNumber, String ldapDomain,
                          String email, String primaryRoleId, List<String> roleIdList)
            throws UserNotFoundException;


    String changeUserTypeToPCCWOrHktUser(String oldUserId, String name, String mobile, String employeeNumber, String email)
            throws InvalidInputException, UserNotFoundException;
    String changeUserTypeToNonPCCWOrHktUser(String oldUserId, String name, String mobile, String employeeNumber, String email)
            throws InvalidInputException, UserNotFoundException;
    String changeUserTypeToLdapUser(String oldUserId, String name, String mobile, String employeeNumber, String ldapDomain, String email)
            throws InvalidInputException, UserNotFoundException;


    void updateUserPwd(String userId, String rawOldPassword, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException;
    void resetPwd(String otp, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException, InvalidInputException;
    void requestResetPassword(String username) throws UserNotFoundException, MessagingException;
    void disableUserByUsername(String username) throws UserNotFoundException, InvalidInputException;
    void updateUser(String userId, String newName, String newMobile, String email, String primaryRoleId, List<String> userRoleIdList)
            throws UserNotFoundException, InsufficientAuthorityException, GeneralSecurityException;


    String getCurrentUserUserId() throws UserNotFoundException;
    SdUserBean getCurrentSdUserBean() throws UserNotFoundException;
    SdUserBean getUserByUserId(String userId) throws UserNotFoundException;


    Page<SdUserBean> searchUser(Pageable pageable, String userId, String email, String name);

    Page<SdUserBean> getTeamHeadUser(Pageable pageable, String teamHead);

    void cleanOutDatePwdHistData();

    List<SdUserBean> getUserByRoleId(String roleId);
}
