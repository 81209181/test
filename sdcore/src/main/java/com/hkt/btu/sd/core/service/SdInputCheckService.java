package com.hkt.btu.sd.core.service;


import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.bean.SdUserRoleBean;

import java.util.List;

public interface SdInputCheckService {
    String checkName(String input) throws InvalidInputException;
    String checkCompanyName(String input) throws InvalidInputException;
    String checkMobile(String input) throws InvalidInputException;
    String checkStaffIdHkidPassport(String input) throws InvalidInputException;
    String checkEmail(String input) throws InvalidInputException;
    String checkEmployeeNumber(String input) throws InvalidInputException;
    String checkLdapDomain(String input) throws  InvalidInputException;
    String checkUserName(String input) throws InvalidInputException;
    String checkAssignRoleByDomain(List<String> roleList, String domain) throws InvalidInputException;

    void checkUserRole(List<String> userRoleIdList, List<SdUserRoleBean> eligibleUserRoleGrantList, List<SdUserRoleBean> userRoleByUserId);
}
