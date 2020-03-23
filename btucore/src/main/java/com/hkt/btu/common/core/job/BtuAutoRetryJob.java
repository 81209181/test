package com.hkt.btu.common.core.job;

import com.hkt.btu.common.core.service.BtuAutoRetryService;
import com.hkt.btu.common.core.service.BtuParamService;
import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import com.hkt.btu.common.core.service.constant.BtuAutoRetryStatusEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@DisallowConcurrentExecution
public class BtuAutoRetryJob extends QuartzJobBean {
    private static final Logger LOG = LogManager.getLogger(BtuAutoRetryJob.class);

    @Autowired
    private ApplicationContext applicationContext;
    @Resource(name = "autoRetryService")
    BtuAutoRetryService autoRetryService;
    @Resource(name = "paramService")
    BtuParamService paramService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        // todo SERVDESK-352: this only re-try 1 row per run...we need to re-try all status=A
        //  loop all records with log stating the progress "Retrying n/224 API call..."
        //  page size set to 100
        Page<BtuAutoRetryBean> RetryQueue = autoRetryService.searchRetryQueue(PageRequest.of(0, 1), null, null, null, null,
                BtuAutoRetryStatusEnum.ACTIVE, null, null, null, null, null, null);

        // todo SERVDESK-352: make a method in BtuAutoRetryServiceImpl to contain below code
        //   void retryMethodCall(List<BtuAutoRetryBean> retryQueueList)
        //   retry ok LOG.info
        //   retry fail LOG.warn
        List<BtuAutoRetryBean> RetryQueueList = RetryQueue.getContent();
        if (CollectionUtils.isEmpty(RetryQueueList)) {
            return;
        }

        RetryQueueList.forEach(autoRetryBean -> {
            try {
                // retry queue from db
                Object bean = applicationContext.getBean(autoRetryBean.getBeanName());
                Object[] objArray = paramService.deserialize(autoRetryBean.getMethodParam());
                Class[] parameterTypes = paramService.getParameterTypes(objArray);
                Method method = bean.getClass().getMethod(autoRetryBean.getMethodName(), parameterTypes);
                method.invoke(bean, objArray);

                // update retry status
                autoRetryService.updateRetryComplete(autoRetryBean.getRetryId());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                LOG.error(e.getMessage(), e);
            }
        });
    }
}