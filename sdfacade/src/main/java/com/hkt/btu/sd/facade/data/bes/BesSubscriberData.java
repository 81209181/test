package com.hkt.btu.sd.facade.data.bes;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

public class BesSubscriberData implements DataInterface {

    public class BOOLEAN_PARAM {
        public static final String YES = "Y";
        public static final String NO = "N";
    }

    private List<BesSubscriberInfoResourceData> subscriberInfos;
    private BesPageData page;

    public List<BesSubscriberInfoResourceData> getSubscriberInfos() {
        return subscriberInfos;
    }

    public void setSubscriberInfos(List<BesSubscriberInfoResourceData> subscriberInfos) {
        this.subscriberInfos = subscriberInfos;
    }

    public BesPageData getPage() {
        return page;
    }

    public void setPage(BesPageData page) {
        this.page = page;
    }

}
