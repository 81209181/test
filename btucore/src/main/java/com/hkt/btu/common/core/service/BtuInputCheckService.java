package com.hkt.btu.common.core.service;


import com.hkt.btu.common.core.exception.InvalidInputException;

public interface BtuInputCheckService {
    String checkName(String input) throws InvalidInputException;
    String checkCompanyName(String input) throws InvalidInputException;
    String checkMobile(String input) throws InvalidInputException;
    String checkStaffIdHkidPassport(String input) throws InvalidInputException;
    String checkEmail(String input) throws InvalidInputException;
}
