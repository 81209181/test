package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.common.core.dao.entity.BtuAutoRetryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SdAutoRetryMapper {

    Integer createAutoRetry(@Param("beanName") String beanName,
                            @Param("methodName") String methodName,
                            @Param("methodParam") String methodParam,
                            @Param("minWaitSecond") Integer minWaitSecond,
                            @Param("status") String status,
                            @Param("tryCount") Integer tryCount,
                            @Param("nextTargetTime") LocalDateTime nextTargetTime,
                            @Param("createby") String createby);

    Integer updateAutoRetry(@Param("retryId") Integer retryId,
                            @Param("beanName") String beanName,
                            @Param("methodName") String methodName,
                            @Param("methodParam") String methodParam,
                            @Param("status") String status,
                            @Param("tryCount") Integer tryCount,
                            @Param("minWaitSecond") Integer minWaitSecond,
                            @Param("nextTargetTime") LocalDateTime nextTargetTime,
                            @Param("modifyby") String modifyby);

    List<BtuAutoRetryEntity> searchRetryQueue(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                              @Param("retryId") Integer retryId,
                                              @Param("beanName") String beanName,
                                              @Param("methodName") String methodName,
                                              @Param("methodParam") String methodParam,
                                              @Param("status") String status,
                                              @Param("tryCountFrom") Integer tryCountFrom,
                                              @Param("tryCountTo") Integer tryCountTo,
                                              @Param("minWaitSecondFrom") Integer minWaitSecondFrom,
                                              @Param("minWaitSecondTo") Integer minWaitSecondTo,
                                              @Param("nextTargetTimeFrom") LocalDateTime nextTargetTimeFrom,
                                              @Param("nextTargetTimeTo") LocalDateTime nextTargetTimeTo);

    Integer searchRetryCount(@Param("retryId") Integer retryId,
                             @Param("beanName") String beanName,
                             @Param("methodName") String methodName,
                             @Param("methodParam") String methodParam,
                             @Param("status") String status,
                             @Param("tryCountFrom") Integer tryCountFrom,
                             @Param("tryCountTo") Integer tryCountTo,
                             @Param("minWaitSecondFrom") Integer minWaitSecondFrom,
                             @Param("minWaitSecondTo") Integer minWaitSecondTo,
                             @Param("nextTargetTimeFrom") LocalDateTime nextTargetTimeFrom,
                             @Param("nextTargetTimeTo") LocalDateTime nextTargetTimeTo);
}