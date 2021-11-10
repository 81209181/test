package com.hkt.btu.common.core.job;

import com.hkt.btu.common.core.service.BtuAutoRetryService;
import com.hkt.btu.common.core.service.bean.BtuAutoRetryBean;
import com.hkt.btu.common.core.service.constant.BtuAutoRetryStatusEnum;
import org.apache.commons.collections.CollectionUtils;
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
	public final int MAX_RETRY_PER_RUN = 100;

	@Resource(name = "autoRetryService")
	BtuAutoRetryService autoRetryService;

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Page<BtuAutoRetryBean> pagedRetryQueue = autoRetryService.searchRetryQueue(
				PageRequest.of(0, MAX_RETRY_PER_RUN),
				null, null, null, null,
				BtuAutoRetryStatusEnum.ACTIVE,
				null, null,
				null, null, null, null);
		LOG.info("Fetched {}/{} retry record(s). (max per run: {})",
				pagedRetryQueue.getNumberOfElements(), pagedRetryQueue.getTotalElements(), MAX_RETRY_PER_RUN);

		List<BtuAutoRetryBean> retryQueueList = pagedRetryQueue.getContent();
		autoRetryService.retryMethodCall(retryQueueList);
	}
}
