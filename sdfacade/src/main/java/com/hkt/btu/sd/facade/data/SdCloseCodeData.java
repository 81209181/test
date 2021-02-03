package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdCloseCodeData implements DataInterface {

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
