package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.annotation.AutoRetry;
import com.hkt.btu.common.core.dao.entity.BtuUserEntity;
import com.hkt.btu.common.core.exception.BtuMissingImplException;
import com.hkt.btu.common.core.service.BtuAutoRetryService;
import com.hkt.btu.common.core.service.BtuParamService;
import com.hkt.btu.common.core.service.BtuUserService;
import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import com.hkt.btu.common.core.service.bean.BtuUserBean;
import com.hkt.btu.common.core.service.constant.BtuAutoRetryStatusEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class BtuAutoRetryServiceImpl implements BtuAutoRetryService {
    private static final Logger LOG = LogManager.getLogger(BtuAutoRetryServiceImpl.class);

    @Resource(name = "paramService")
    BtuParamService btuParamService;
    @Resource(name = "userService")
    BtuUserService userService;



    public Integer createAutoRetry(String clazz, String methodName, String methodParam, int minWaitSecond, LocalDateTime nextTargetTime, String createby){
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    public Integer updateAutoRetry(Integer retryId,
                                   String clazz, String methodName, String methodParam,
                                   BtuAutoRetryStatusEnum statusEnum,
                                   Integer tryCount, Integer minWaitSecond, LocalDateTime nextTargetTime,
                                   String modifyby){
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public Page<BtuAutoRetryBean> searchRetryQueue(
            Pageable pageable, Integer retryId,
            String clazzName, String methodName, String methodParam,
            BtuAutoRetryStatusEnum status,
            Integer tryCountFrom, Integer tryCountTo,
            Integer minWaitSecondFrom, Integer minWaitSecondTo,
            LocalDateTime nextTargetTimeFrom, LocalDateTime nextTargetTimeTo) {
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public Integer queueMethodCallForRetry(Method method, Object[] paramArray) {
        BtuUserBean currentUser = userService.getCurrentUserBean();
        String currentUserId = currentUser==null ? BtuUserEntity.SYSTEM.USER_ID : currentUser.getUserId();

        // serialize invoking class, method, param
        Class clazz = method.getDeclaringClass();
        String clazzName = clazz.getName();
        String methodName = method.getName();
        AutoRetry autoRetry = method.getAnnotation(AutoRetry.class);
        int minWaitSecond = autoRetry==null ? 0 : autoRetry.minWaitSecond();
        LocalDateTime newNextTargetTime = LocalDateTime.now().plusSeconds(minWaitSecond);
        String methodParam = btuParamService.serialize(paramArray);

        // check outstanding retry in queue
        Page<BtuAutoRetryBean> pageBean = searchRetryQueue(
                PageRequest.of(0, 1), null,
                clazzName, methodName, methodParam,
                BtuAutoRetryStatusEnum.ACTIVE,
                null, null,
                null, null,
                null, null
                );

        BtuAutoRetryBean existingAutoRetryBean = pageBean.getTotalElements() < 1 ? null : pageBean.getContent().get(0);
        if(existingAutoRetryBean==null){
            return createAutoRetry(clazzName, methodName, methodParam, minWaitSecond, newNextTargetTime, currentUserId);
        }else{
            Integer newTryCount = existingAutoRetryBean.getTryCount() + 1;

            updateAutoRetry( existingAutoRetryBean.getRetryId(),
                    null, null, null,
                    null,
                    newTryCount, minWaitSecond, newNextTargetTime,
                    currentUserId );
            return existingAutoRetryBean.getRetryId();
        }
    }

    @Override
    public boolean updateRetryComplete(Integer retryId) {
        BtuUserBean currentUser = userService.getCurrentUserBean();
        String currentUserId = currentUser==null ? BtuUserEntity.SYSTEM.USER_ID : currentUser.getUserId();

        int updateCount = updateAutoRetry( retryId,
                null, null, null,
                BtuAutoRetryStatusEnum.CANCELLED,
                null, null, null,
                currentUserId );

        if(updateCount>0){
            LOG.info("Retry completed. (retryId={})", retryId);
            return true;
        }else{
            LOG.error("Cannot update retry complete. (retryId={})", retryId);
            return false;
        }
    }
}
