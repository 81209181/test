package com.hkt.btu.common.facade.data;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageData<T extends DataInterface> extends PageImpl<T> implements PageDataInterface<T> {

    private String errorMsg;

    public PageData(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageData(String errorMsg){
        super(List.of());
        this.errorMsg = errorMsg;
    }



    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
