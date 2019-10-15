package com.hkt.btu.sd.facade.data;

import com.hkt.btu.common.facade.data.DataInterface;

import java.util.List;

@Deprecated // todo: EditResultData<D extends DataInterface>
public class EditResultData {

    private String errorMsg;
    private List<?> list; // todo: List<D> list

    public static EditResultData error(String errorMsg) {
        EditResultData data = new EditResultData();
        data.setErrorMsg(errorMsg);
        return data;
    }

    public static EditResultData dataList(List<?> list) {
        EditResultData data = new EditResultData();
        data.setList(list);
        return data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
