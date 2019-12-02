package com.hkt.btu.sd.core.service.bean;

import java.util.StringJoiner;

public class SdSortBean {

    private String column;
    private String dir;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SdSortBean.class.getSimpleName() + "[", "]")
                .add("column='" + column + "'")
                .add("dir='" + dir + "'")
                .toString();
    }
}
