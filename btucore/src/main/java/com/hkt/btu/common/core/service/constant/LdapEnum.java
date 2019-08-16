package com.hkt.btu.common.core.service.constant;

import com.hkt.btu.common.spring.security.exception.NotPermittedLogonException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum LdapEnum {
    CSL("OA-HKCSL-NET", "ldaps://oa.hkcsl.net:636/",
            "OU=cslcorp,DC=oa,DC=hkcsl,DC=net", "@oa.hkcsl.net"),
    PCCWS("CORP", "ldaps://ldaps.corp.root:636/", 
            "OU=TSB,DC=CORP,DC=ROOT", "@corp.root"),
    PCCW("CORPHQ-HK-PCCW", "ldaps://ldaps.corphq.hk.pccw.com:636/", 
            "DC=corphq,DC=hk,DC=pccw,DC=com", "@corphq.hk.pccw.com")
    ;

    private static final Map<String, LdapEnum> LDAP_DOMAIN_LIST = Collections.unmodifiableMap
            (Arrays.stream(LdapEnum.values()).
                    collect(Collectors.toMap(constant -> constant.getPrincipalName(), constant -> constant)));

    LdapEnum(String displayName, String hostUrl, String base, String principalName) {
        this.displayName = displayName;
        this.hostUrl = hostUrl;
        this.base = base;
        this.principalNameSuffix = principalName;
    }

    public static LdapEnum getValue(String domain) {
        String checkDomain = Optional.ofNullable(domain).orElseThrow(() -> new NotPermittedLogonException(""));
        return LDAP_DOMAIN_LIST.get(checkDomain);
    }

    private String displayName;
    private String hostUrl;
    private String base;
    private String principalNameSuffix;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getPrincipalName() {
        return principalNameSuffix;
    }

    public void setPrincipalName(String principalName) {
        this.principalNameSuffix = principalName;
    }
}
