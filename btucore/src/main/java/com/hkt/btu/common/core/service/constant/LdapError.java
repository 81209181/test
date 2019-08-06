package com.hkt.btu.common.core.service.constant;

/**
 * Ldap Error Code
 */
public enum LdapError {
    USER_NOT_FOUND("525", "User not found in directory"),
    INVALID_LOGIN("52e", "Invalid login credentials"),
    NOT_PERMITTED_LOGIN_TIME("530", "Not permitted to logon at this time. Please contact administrator."),
    NOT_PERMITTED_LOGIN_WORKSTATION("531", "Not permitted to logon at this workstation"),
    PWD_EXPIRED("532", "Password has expired. Please change your password."),
    ACCOUNT_DISABLED("533", "Your account is disabled. Please contact administrator."),
    ACCOUNT_EXPIRED("701", "Your account has expired. Please contact administrator."),
    CHANGE_PWD("773", "Please change your password."),
    LOCK("775", "Your account is locked. Please contact administrator.");

    private String code;
    private String msg;

    LdapError(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
