package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.exception.InvalidInputException;
import com.hkt.btu.sd.core.service.SdInputCheckService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.nio.charset.StandardCharsets;


public class SdInputCheckServiceImpl implements SdInputCheckService {

    @Override
    public String checkName(String input) throws InvalidInputException {
        if (StringUtils.isEmpty(input)) {
            throw new InvalidInputException("Empty input name.");
        } else if ( StringUtils.isNumeric(input) ) {
            throw new InvalidInputException("Cannot use numeric name.");
        } else {
            int byteLength = input.getBytes(StandardCharsets.UTF_8).length;
            if ( byteLength<6 ) {
                throw new InvalidInputException("Input name too short.");
            } else if ( byteLength>100 ) {
                throw new InvalidInputException("Input name too long.");
            }
        }

        return null;
    }

    @Override
    public String checkCompanyName(String input) throws InvalidInputException {
        if (StringUtils.isEmpty(input)) {
            throw new InvalidInputException("Empty input company name.");
        } else if ( StringUtils.isNumeric(input) ) {
            throw new InvalidInputException("Cannot use numeric company name.");
        } else {
            int byteLength = input.getBytes(StandardCharsets.UTF_8).length;
            if ( byteLength<6 ) {
                throw new InvalidInputException("Input company name too short.");
            } else if ( byteLength>100 ) {
                throw new InvalidInputException("Input company name too long.");
            }
        }

        return null;
    }

    @Override
    public String checkMobile(String input) throws InvalidInputException {
        if (StringUtils.isEmpty(input)) {
            throw new InvalidInputException("Empty input mobile.");
        } else if ( input.length()<8 || input.length()>15 ) {
            throw new InvalidInputException("Please input mobile with 8-15 digits.");
        }

        return null;
    }

    @Override
    public String checkStaffIdHkidPassport(String input) throws InvalidInputException {
        if (StringUtils.isEmpty(input)) {
            throw new InvalidInputException("Empty input Staff ID / HKID / Passport Number.");
        } else if ( input.length()!=4 ) {
            throw new InvalidInputException("Please input only first 4 digits of your Staff ID / HKID / Passport Number.");
        }
        return null;
    }

    @Override
    public String checkEmail(String input) throws InvalidInputException {
        if (StringUtils.isEmpty(input)) {
            throw new InvalidInputException("Empty input Email.");
        } else if ( ! EmailValidator.getInstance().isValid(input) ) {
            throw new InvalidInputException("Please input a valid email.");
        }
        return null;
    }

    @Override
    public String checkLdapDomain(String input) throws InvalidInputException {
        if (StringUtils.isEmpty(input)) {
            throw new InvalidInputException("Empty input Ldap Domain.");
        }
        return null;
    }
}
