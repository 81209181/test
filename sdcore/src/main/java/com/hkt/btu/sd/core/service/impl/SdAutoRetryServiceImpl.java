package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import com.hkt.btu.common.core.service.constant.BtuAutoRetryStatusEnum;
import com.hkt.btu.common.core.service.impl.BtuAutoRetryServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public class SdAutoRetryServiceImpl extends BtuAutoRetryServiceImpl {
    private static final Logger LOG = LogManager.getLogger(SdAutoRetryServiceImpl.class);

    public Integer createAutoRetry(String clazz, String methodName, String methodParam, int minWaitSecond, String createby){
        // todo [SERVDESK-350]: return new retry id
        return 0;
    }

    public Integer updateAutoRetry(Integer retryId,
                                   String clazz, String methodName, String methodParam,
                                   BtuAutoRetryStatusEnum statusEnum,
                                   Integer tryCount, Integer minWaitSecond, LocalDateTime nextTargetTime,
                                   String modifyby){
        // todo [SERVDESK-350]: return update row count
        return 1;
    }

    @Override
    public Page<BtuAutoRetryBean> searchRetryQueue(
            Pageable pageable, Integer retryId,
            String clazzName, String methodName, String methodParam,
            BtuAutoRetryStatusEnum status,
            Integer tryCountFrom, Integer tryCountTo,
            Integer minWaitSecondFrom, Integer minWaitSecondTo,
            LocalDateTime nextTargetTimeFrom, LocalDateTime nextTargetTimeTo) {
        // todo [SERVDESK-350]: return paged result
        // if null, no need to add in sql where cause
        // order by nextTargetTime desc
        return null;
    }
}
