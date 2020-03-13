package com.hkt.btu.common.core.service;

import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import com.hkt.btu.common.core.service.constant.BtuAutoRetryStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

public interface BtuAutoRetryService {

    Integer createAutoRetry(String clazz, String methodName, String methodParam, int minWaitSecond, String createby);

    Integer updateAutoRetry(Integer retryId,
                            String clazz, String methodName, String methodParam,
                            BtuAutoRetryStatusEnum statusEnum,
                            Integer tryCount, Integer minWaitSecond, LocalDateTime nextTargetTime,
                            String modifyby);

    Page<BtuAutoRetryBean> searchRetryQueue(
            Pageable pageable, Integer retryId,
            String clazzName, String methodName, String methodParam,
            BtuAutoRetryStatusEnum status,
            Integer tryCountFrom, Integer tryCountTo,
            Integer minWaitSecondFrom, Integer minWaitSecondTo,
            LocalDateTime nextTargetTimeFrom, LocalDateTime nextTargetTimeTo );

    Integer queueMethodCallForRetry(Class clazz, Method method, Object[] paramArray, int minWaitSecond);

    boolean updateRetryComplete(Integer retryId);

}
