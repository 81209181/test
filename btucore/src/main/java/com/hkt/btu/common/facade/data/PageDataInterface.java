package com.hkt.btu.common.facade.data;

import org.springframework.data.domain.Page;

@Deprecated // replaced by BtuPageData
public interface PageDataInterface<T> extends Page<T> {
    String getErrorMsg();
}
