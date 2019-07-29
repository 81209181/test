package com.hkt.btu.noc.facade;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.noc.facade.data.*;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;

public interface NocUserFacade {
    NocUserData getCurrentUser();
    NocUserData getUserByUserId(Integer userId);

    boolean isInternalUser();


    CreateResultData createUser(CreateUserFormData createUserFormData);
    LinkedList<NocCompanyData> getEligibleCompanyList();

    String updateUser(UpdateUserFormData updateUserFormData);

    String activateUser(Integer userId);
    String deactivateUser(Integer userId);

    String updateCurrentUserPwd(UpdatePwdFormData updatePwdFormData);
    String resetPassword(ResetPwdFormData resetPwdFormData);
    String requestResetPassword(String email);

    PageData<NocUserData> searchUser(Pageable pageable, Integer userId, String email, String name, String userGroupId);
}
