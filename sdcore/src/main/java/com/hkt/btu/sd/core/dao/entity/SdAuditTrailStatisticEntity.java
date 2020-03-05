package com.hkt.btu.sd.core.dao.entity;

import java.util.StringJoiner;

public class SdAuditTrailStatisticEntity {

    private int total;
    private String statisticDate;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatisticDate() {
        return statisticDate;
    }

    public void setStatisticDate(String statisticDate) {
        this.statisticDate = statisticDate;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SdAuditTrailStatisticEntity.class.getSimpleName() + "[", "]")
                .add("total=" + total)
                .add("statisticDate=" + statisticDate)
                .toString();
    }
}
