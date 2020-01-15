package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

public class SdApiUserData implements DataInterface {

    public static class CACHE_SYNC{
        public static final String YES = "Y";
        public static final String NO = "N";
    }

    private String name;

    private String cacheSync;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCacheSync() {
        return cacheSync;
    }

    public void setCacheSync(String cacheSync) {
        this.cacheSync = cacheSync;
    }
}
