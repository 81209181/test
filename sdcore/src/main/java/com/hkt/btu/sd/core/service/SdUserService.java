package com.hkt.btu.sd.core.service;


import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface SdUserService extends BtuUserService {

    String getCurrentUserUserId() throws UserNotFoundException;

    Integer getCompanyIdRestriction() throws AuthorityNotFoundException;

    String getUserIdRestriction() throws AuthorityNotFoundException;

    boolean isInternalUser();

    boolean isAdminUser();

    SdUserBean getUserByUserId(String userId) throws UserNotFoundException;

    Page<SdUserBean> searchUser(Pageable pageable, String userId, String email, String name);

    void updateUserPwd(String userId, String rawOldPassword, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException;

    void resetPwd(String otp, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException, InvalidInputException;

    void disableUserByUsername(String username) throws UserNotFoundException, InvalidInputException;


    String createUser(String name, String mobile, String email, List<String> roleIdList)
            throws DuplicateUserEmailException, UserNotFoundException, GeneralSecurityException;

    String createLdapUser(String name, String mobile, String employeeNumber, String ldapDomain,
                          List<String> roleIdList)
            throws DuplicateUserEmailException, UserNotFoundException;

    void updateUser(String userId, String newName, String newMobile, List<String> userRoleIdList)
            throws UserNotFoundException, InsufficientAuthorityException, GeneralSecurityException;

    void requestResetPassword(String username) throws UserNotFoundException, MessagingException;
}
