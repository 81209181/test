package com.hkt.btu.sd.core.service.bean;

import com.hkt.btu.common.core.service.bean.BaseBean;

public class SdCloseCodeBean extends BaseBean {

    private String closeCode;
    private String closeCodeDescription;

    public String getCloseCode() {
        return closeCode;
    }

    public void setCloseCode(String closeCode) {
        this.closeCode = closeCode;
    }

    public String getCloseCodeDescription() {
        return closeCodeDescription;
    }

    public void setCloseCodeDescription(String closeCodeDescription) {
        this.closeCodeDescription = closeCodeDescription;
    }
}
