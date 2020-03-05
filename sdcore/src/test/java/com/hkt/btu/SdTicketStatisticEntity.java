package com.hkt.btu;

public class SdTicketStatisticEntity {

    private String statisticDate;
    private int totalCount;
    private String statistics;
    private String owningRole;

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

    public String getOwningRole() {
        return owningRole;
    }

    public void setOwningRole(String owningRole) {
        this.owningRole = owningRole;
    }
}
