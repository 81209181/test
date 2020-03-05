package com.hkt.btu.sd.core.service.bean;

import java.util.List;

public class SdTicketChartBean {

    private int max;
    private List<String> header;
    private List<String[]> statisticList;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<String[]> getStatisticList() {
        return statisticList;
    }

    public void setStatisticList(List<String[]> statisticList) {
        this.statisticList = statisticList;
    }
}
