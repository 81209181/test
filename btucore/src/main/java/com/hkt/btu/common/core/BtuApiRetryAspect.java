package com.hkt.btu.common.core;

import com.hkt.btu.common.core.annotation.AutoRetry;
import com.hkt.btu.common.core.service.BtuAutoRetryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Method;


/**
 * Prerequisite:
 *   - API call method must be asynchronous (if not, this implies procedural flow and retry cannot be automatic)
 *   - API call method must return void (if not, this implies dependency of return value, but automatic retry cannot
 *     find generic successor of the return value)
 *
 * @author  Jason Kong
 */
@Aspect
@Configuration
public class BtuApiRetryAspect {
	private static final Logger LOG = LogManager.getLogger(BtuJobAspect.class);

	@Resource(name = "btuAutoRetryService")
	BtuAutoRetryService btuAutoRetryService;


	@Around("execution(public void com.hkt.btu.*.facade.impl.*ApiFacadeImpl.*(..)) && @annotation(com.hkt.btu.common.core.annotation.AutoRetry)")
	public void aroundApiCall(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		try {
			proceedingJoinPoint.proceed();

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			// gather caller info
			LOG.info("Gathering origin method call info...");
			MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
			Method method = signature.getMethod();
			Object[] paramArray = proceedingJoinPoint.getArgs();

			// add to api retry queue
			Integer retryId = btuAutoRetryService.queueMethodCallForRetry(method, paramArray);
			LOG.info("Queue API call for retry. (retryId={})", retryId);
		}
	}
}