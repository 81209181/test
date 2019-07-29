package com.hkt.btu.common.core.service.constant;

public enum LdapEnum {
    CSL("OA-HKCSL-NET", "ldaps://oa.hkcsl.net:636/", 
            "OU=cslcorp,DC=oa,DC=hkcsl,DC=net", "@oa.hkcsl.net"),
    PCCWS("CORP", "ldaps://ldaps.corp.root:636/", 
            "OU=TSB,DC=CORP,DC=ROOT", "@corp.root"),
    PCCW("CORPHQ-HK-PCCW", "ldaps://ldaps.corphq.hk.pccw.com:636/", 
            "DC=corphq,DC=hk,DC=pccw,DC=com", "@corphq.hk.pccw.com")
    ;


    LdapEnum(String displayName, String hostUrl, String base, String principalName) {
        this.displayName = displayName;
        this.hostUrl = hostUrl;
        this.base = base;
        this.principalNameSuffix = principalName;
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
