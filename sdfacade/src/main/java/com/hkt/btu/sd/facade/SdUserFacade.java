package com.hkt.btu.sd.facade;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.*;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;

public interface SdUserFacade {
    SdUserData getCurrentUser();

    SdUserData getUserByUserId(String userId);

    CreateResultData createUser(CreateUserFormData createUserFormData);

    CreateResultData createNonPccwHktUser(CreateUserFormData createUserFormData);

    CreateResultData createLdapUser(CreateUserFormData createUserFormData);

    String updateUser(UpdateUserFormData updateUserFormData);

    ChangeUserTypeResultData changeUserTypeToPccwOrHktUser(ChangeUserTypeFormData changeUserTypeFormData);

    ChangeUserTypeResultData changeUserTypeToNonPccwOrHktUser(ChangeUserTypeFormData changeUserTypeFormData);

    ChangeUserTypeResultData changeUserTypeToLdapUser(ChangeUserTypeFormData changeUserTypeFormData);

    String activateUser(String userId);

    String deactivateUser(String userId);

    String updateCurrentUserPwd(UpdatePwdFormData updatePwdFormData);

    String resetPassword(ResetPwdFormData resetPwdFormData);

    String requestResetPassword(String email);

    PageData<SdUserData> searchUser(Pageable pageable, String userId, String email, String name);

    void resetPwd4NonLdapUser(String userId) throws MessagingException;
}
