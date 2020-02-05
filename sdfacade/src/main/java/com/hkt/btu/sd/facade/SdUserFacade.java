package com.hkt.btu.sd.facade;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.*;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;

public interface SdUserFacade {
    SdUserData getCurrentUser();

    SdUserData getUserByUserId(String userId);

    SdCreateResultData createPccwHktUser(SdCreateUserFormData createUserFormData);

    SdCreateResultData createNonPccwHktUser(SdCreateUserFormData createUserFormData);

    SdCreateResultData createLdapUser(SdCreateUserFormData createUserFormData);

    String updateUser(SdUpdateUserFormData updateUserFormData);

    SdChangeUserTypeResultData changeUserTypeToPccwOrHktUser(SdChangeUserTypeFormData changeUserTypeFormData);

    SdChangeUserTypeResultData changeUserTypeToNonPccwOrHktUser(SdChangeUserTypeFormData changeUserTypeFormData);

    SdChangeUserTypeResultData changeUserTypeToLdapUser(SdChangeUserTypeFormData changeUserTypeFormData);

    String activateUser(String userId);

    String deactivateUser(String userId);

    String updateCurrentUserPwd(SdUpdatePwdFormData updatePwdFormData);

    String resetPassword(SdResetPwdFormData resetPwdFormData);

    String requestResetPassword(String email);

    PageData<SdUserData> searchUser(Pageable pageable, String userId, String email, String name);

    void resetPwd4NonLdapUser(String userId) throws MessagingException;

    PageData<SdUserData> getTeamHeadUser(Pageable pageable, String teamHead);
}
