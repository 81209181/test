package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.*;

public class UTCallActionData implements DataInterface {
    private List<Map<String, String>> UT_SUMMARY;

    public List<Map<String, String>> getUT_SUMMARY() {
        return UT_SUMMARY;
    }

    public void setUT_SUMMARY(List<Map<String, String>> UT_SUMMARY) {
        this.UT_SUMMARY = UT_SUMMARY;
    }
}