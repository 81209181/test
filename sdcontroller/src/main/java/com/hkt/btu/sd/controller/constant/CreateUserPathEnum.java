package com.hkt.btu.sd.controller.constant;

import com.hkt.btu.common.core.service.constant.LdapEnum;
import com.hkt.btu.common.spring.security.exception.NotPermittedLogonException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum CreateUserPathEnum {
    LDAP_USER("/admin/manage-user/create-ldap-user", "admin/manageUser/createLdapUserForm"),
    PCCW_HTK_USER("/admin/manage-user/create-user", "admin/manageUser/createPccwOrHktUserForm"),
    NON_PCCW_HKT_USER("/admin/manage-user/create-non-user", "admin/manageUser/createNonPccwOrHktUserForm");

    private static final Map<String, String> USER_PATH = Collections.unmodifiableMap
            (Arrays.stream(CreateUserPathEnum.values()).
                    collect(Collectors.toMap(constant -> constant.getPagePath(), constant -> constant.getCreateFormName())));

    CreateUserPathEnum(String pagePath, String createFormName) {
        this.pagePath = pagePath;
        this.createFormName = createFormName;
    }

    public static String getValue(String path) {
        return USER_PATH.get(path);
    }


    private String pagePath;
    private String createFormName;

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getCreateFormName() {
        return createFormName;
    }

    public void setCreateFormName(String createFormName) {
        this.createFormName = createFormName;
    }
}
