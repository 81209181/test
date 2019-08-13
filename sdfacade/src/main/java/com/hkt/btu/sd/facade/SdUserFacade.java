package com.hkt.btu.sd.facade;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.*;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;

public interface SdUserFacade {
    SdUserData getCurrentUser();
    SdUserData getUserByUserId(String userId);

    boolean isInternalUser();


    CreateResultData createUser(CreateUserFormData createUserFormData);

    String updateUser(UpdateUserFormData updateUserFormData);

    String activateUser(String userId);
    String deactivateUser(String userId);

    String updateCurrentUserPwd(UpdatePwdFormData updatePwdFormData);
    String resetPassword(ResetPwdFormData resetPwdFormData);
    String requestResetPassword(String email);

    PageData<SdUserData> searchUser(Pageable pageable, String userId, String email, String name, String userGroupId);
}
