package com.hkt.btu.sd.core.service;


import com.hkt.btu.common.core.exception.UserNotFoundException;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.sd.core.exception.*;
import com.hkt.btu.sd.core.service.bean.SdCompanyBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface SdUserService extends BtuUserService {

    Integer getCurrentUserUserId() throws UserNotFoundException;

    Integer getCompanyIdRestriction() throws AuthorityNotFoundException;
    Integer getUserIdRestriction() throws AuthorityNotFoundException;
    boolean isInternalUser();
    boolean isAdminUser();

    SdUserBean getUserByUserId(Integer userId) throws UserNotFoundException;

    Page<SdUserBean> searchUser(Pageable pageable, Integer userId, String email, String name, String userGroupId);

    void updateUserPwd(Integer userId, String rawOldPassword, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException;
    void resetPwd(String otp, String rawNewPassword)
            throws UserNotFoundException, InvalidPasswordException, InvalidInputException;

    void disableUserByUsername(String username) throws UserNotFoundException, InvalidInputException;


    Integer createUser(String name, String mobile, String email, String staffId,
                       Integer companyId, List<String> groupIdList)
            throws DuplicateUserEmailException, UserNotFoundException, GeneralSecurityException;
    void updateUser(Integer userId, String newName, String newMobile, String newStaffId,
                    Boolean isNewAdmin, Boolean isNewUser, Boolean isNewCAdmin, Boolean isNewCUser)
            throws UserNotFoundException, InsufficientAuthorityException, GeneralSecurityException;

    List<SdCompanyBean> getEligibleCompanyList();

    void requestResetPassword(String username) throws UserNotFoundException, MessagingException;
}
