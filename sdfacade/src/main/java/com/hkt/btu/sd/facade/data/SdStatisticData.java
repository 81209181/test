package com.hkt.btu.sd.facade.data;

import java.util.List;

public class SdStatisticData<T> {

    private int maxTotal;
    private List<T> data;

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
