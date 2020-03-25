package com.hkt.btu.common.core.job;

import com.hkt.btu.common.core.service.BtuAutoRetryService;
import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import com.hkt.btu.common.core.service.constant.BtuAutoRetryStatusEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;

@DisallowConcurrentExecution
public class BtuAutoRetryJob extends QuartzJobBean {
    private static final Logger LOG = LogManager.getLogger(BtuAutoRetryJob.class);

    @Resource(name = "autoRetryService")
    BtuAutoRetryService autoRetryService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Page<BtuAutoRetryBean> RetryQueue = autoRetryService.searchRetryQueue(PageRequest.of(0, 100), null, null, null, null,
                BtuAutoRetryStatusEnum.ACTIVE, null, null, null, null, null, null);

        List<BtuAutoRetryBean> RetryQueueList = RetryQueue.getContent();
        autoRetryService.retryMethodCall(RetryQueueList);
    }
}