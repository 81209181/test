package com.hkt.btu.sd.facade.data.bes;

import com.hkt.btu.common.facade.data.DataInterface;

public class BesPageData implements DataInterface {

    private long pageSize;
    private long startNum;
    private long totalNum;


    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getStartNum() {
        return startNum;
    }

    public void setStartNum(long startNum) {
        this.startNum = startNum;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

}
