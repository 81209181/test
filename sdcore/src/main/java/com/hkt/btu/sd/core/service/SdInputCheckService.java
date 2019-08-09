package com.hkt.btu.sd.core.service;


import com.hkt.btu.sd.core.exception.InvalidInputException;

public interface SdInputCheckService {
    String checkName(String input) throws InvalidInputException;
    String checkCompanyName(String input) throws InvalidInputException;
    String checkMobile(String input) throws InvalidInputException;
    String checkStaffIdHkidPassport(String input) throws InvalidInputException;
    String checkEmail(String input) throws InvalidInputException;
    String checkLdapDomain(String input) throws InvalidInputException;
}
