package com.hkt.btu.noc.core.service;


import com.hkt.btu.noc.core.exception.InvalidInputException;

public interface NocInputCheckService {
    String checkName(String input) throws InvalidInputException;
    String checkCompanyName(String input) throws InvalidInputException;
    String checkMobile(String input) throws InvalidInputException;
    String checkStaffIdHkidPassport(String input) throws InvalidInputException;
    String checkEmail(String input) throws InvalidInputException;
}
