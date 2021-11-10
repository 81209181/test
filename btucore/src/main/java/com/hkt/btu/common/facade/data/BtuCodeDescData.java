package com.hkt.btu.common.facade.data;

public class BtuCodeDescData implements DataInterface {
    private String code;
    private String codeDesc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }
}
