package com.hkt.btu.sd.core.dao.entity;

import java.util.StringJoiner;

public class SdTicketStatisticEntity {

    private String statisticDate;
    private int totalCount;
    private String statistics;

    public String getStatisticDate() {
        return statisticDate;
    }

    public void setStatisticDate(String statisticDate) {
        this.statisticDate = statisticDate;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SdTicketStatisticEntity.class.getSimpleName() + "[", "]")
                .add("statisticDate='" + statisticDate + "'")
                .add("totalCount=" + totalCount)
                .add("statistics='" + statistics + "'")
                .toString();
    }
}
