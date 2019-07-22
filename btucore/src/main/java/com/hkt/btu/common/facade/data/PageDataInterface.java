package com.hkt.btu.common.facade.data;

import org.springframework.data.domain.Page;

@SuppressWarnings("WeakerAccess")
public interface PageDataInterface<T> extends Page<T> {
    String getErrorMsg();
}
