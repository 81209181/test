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
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

public class BtuAutoRetryServiceImpl implements BtuAutoRetryService {
    private static final Logger LOG = LogManager.getLogger(BtuAutoRetryServiceImpl.class);

    @Autowired
    ApplicationContext applicationContext;

    @Resource(name = "paramService")
    BtuParamService btuParamService;
    @Resource(name = "userService")
    BtuUserService userService;



    public Integer createAutoRetry(String clazz, String methodName, String methodParam, int minWaitSecond, LocalDateTime nextTargetTime, String createby){
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    public Integer updateAutoRetry(Integer retryId,
                                   String beanName, String methodName, String methodParam,
                                   BtuAutoRetryStatusEnum statusEnum,
                                   Integer tryCount, Integer minWaitSecond, LocalDateTime nextTargetTime,
                                   String modifyby){
        LOG.error("DEMO ONLY IMPLEMENTATION! Please override and implement by DI.");
        throw new BtuMissingImplException();
    }

    @Override
    public Page<BtuAutoRetryBean> searchRetryQueue(
            Pageable pageable, Integer retryId,
            String beanName, String methodName, String methodParam,
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
        String beanName = applicationContext.getBeanNamesForType(clazz)[0];
        String methodName = method.getName();
        AutoRetry autoRetry = method.getAnnotation(AutoRetry.class);
        int minWaitSecond = autoRetry==null ? 0 : autoRetry.minWaitSecond();
        LocalDateTime nextTargetTime = LocalDateTime.now().plusSeconds(minWaitSecond);
        String methodParam = btuParamService.serialize(paramArray);

        // check outstanding retry in queue
        Page<BtuAutoRetryBean> pageBean = searchRetryQueue(
                PageRequest.of(0, 1), null,
                beanName, methodName, methodParam,
                BtuAutoRetryStatusEnum.ACTIVE,
                null, null,
                null, null,
                null, null
                );

        BtuAutoRetryBean existingAutoRetryBean = pageBean.getTotalElements() < 1 ? null : pageBean.getContent().get(0);
        if(existingAutoRetryBean==null){
            return createAutoRetry(beanName, methodName, methodParam, minWaitSecond, nextTargetTime, currentUserId);
        }else{
            Integer newTryCount = existingAutoRetryBean.getTryCount() + 1;
            LocalDateTime newNextTargetTime = existingAutoRetryBean.getNextTargetTime().plusSeconds(minWaitSecond);

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
                BtuAutoRetryStatusEnum.COMPLETED,
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

    @Override
    public void retryMethodCall(List<BtuAutoRetryBean> retryQueueList) {
        if (CollectionUtils.isEmpty(retryQueueList)) {
            LOG.warn("No record in retry queue.");
            return;
        }

        retryQueueList.forEach(autoRetryBean -> {
            LOG.info(String.format("Retrying %d/%d API call...", retryQueueList.indexOf(autoRetryBean)+1, retryQueueList.size()));
            try {
                // retry queue from db
                Object bean = applicationContext.getBean(autoRetryBean.getBeanName());
                Object[] objArray = btuParamService.deserialize(autoRetryBean.getMethodParam());
                Class[] parameterTypes = btuParamService.getParameterTypes(objArray);
                Method method = bean.getClass().getMethod(autoRetryBean.getMethodName(), parameterTypes);
                method.invoke(bean, objArray);

                // update retry status
                updateRetryComplete(autoRetryBean.getRetryId());
                LOG.info("Retry API call success.");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                LOG.warn("Retry API call fail.");
                LOG.error(e.getMessage(), e);
            }
        });
    }
}
