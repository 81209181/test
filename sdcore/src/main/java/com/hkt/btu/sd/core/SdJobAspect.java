package com.hkt.btu.sd.core;

import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdCronJobLogService;
import com.hkt.btu.sd.core.service.SdCronJobProfileService;
import com.hkt.btu.sd.core.service.SdEmailService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Aspect
@Configuration
public class SdJobAspect {
    private static final Logger LOG = LogManager.getLogger(SdJobAspect.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource(name = "cronJobProfileService")
    SdCronJobProfileService sdCronJobProfileService;
    @Resource(name = "cronJobLogService")
    SdCronJobLogService sdCronJobLogService;
    @Resource(name = "emailService")
    SdEmailService sdEmailService;
    @Resource(name = "configParamService")
    SdConfigParamService sdConfigParamService;


    private JobExecutionContext getJobExecutionContext(Object[] args) throws JobExecutionException {
        if(args==null){
            throw new JobExecutionException("Null args.");
        }

        for(Object arg : args){
            if(arg instanceof JobExecutionContext){
                return (JobExecutionContext) arg;
            }
        }
        throw new JobExecutionException("Null jobExecutionContext.");
    }

    @Around("execution(* com.hkt.btu.sd.core.job.*.executeInternal(..))")
    public Object aroundExecuteInternal(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        JobExecutionContext jobExecutionContext;
        JobDetail jobDetail;
        JobKey jobKey;

        // get job context
        try {
            jobExecutionContext = getJobExecutionContext(proceedingJoinPoint.getArgs());
            jobDetail = jobExecutionContext.getJobDetail();
            jobKey = jobDetail.getKey();
        } catch (Exception e) {
            LOG.error("Invalid job context!");
            LOG.error(e.getMessage(), e);
            throw new SchedulerException("Invalid job context!");
        }

        /*
         *  CronTrigger:    SdSchedulerService.scheduleJob(*).createCronTrigger(*)
         *  SimpleTrigger:  SdSchedulerService.triggerJob(*)
         *  */
        boolean isCronTrigger = jobExecutionContext.getTrigger() instanceof CronTrigger;
        if(isCronTrigger) {
            // check if job should be run
            try {
                boolean isRunnable = sdCronJobProfileService.isRunnable(jobKey.getGroup(), jobKey.getName());
                if (!isRunnable) {
                    LOG.warn("Skipped non-runnable job: " + jobKey);
                    sdCronJobLogService.logSkip(jobDetail);
                    return null;
                }
            } catch (Exception e) {
                LOG.error("Invalid job profile!");
                LOG.error(e.getMessage(), e);
                throw new SchedulerException("Invalid job profile!");
            }
        }

        // job start
        try {
            LOG.info("========== Starting Job ==========");
            LOG.info("Job name: "       + jobKey.getName());
            LOG.info("Group name: "     + jobKey.getGroup());
            LOG.info("Trigger key: "   + jobExecutionContext.getTrigger().getKey());
            LOG.info("Firing time: "    + jobExecutionContext.getFireTime());

            Object returnObject = proceedingJoinPoint.proceed(); // executeInternal(..)
            sdCronJobLogService.logComplete(jobDetail);
            return returnObject;
        } catch (Exception e){
            LOG.error("Cannot complete job: " + jobKey);
            LOG.error(e.getMessage(), e);
            sdCronJobLogService.logError(jobDetail);
            emailJobError(e, jobKey.getName(), jobExecutionContext.getFireTime());
            throw new SchedulerException("Cannot complete job: " + jobKey);
        } finally {
            LOG.info("=========== Ending Job ===========");
        }
    }

    private void emailJobError(Exception error, String jobName, Date jobFireTime){
        try{
            // find recipient
            String recipient = sdConfigParamService.getString(
                    SdConfigParamEntity.CRONJOB.CONFIG_GROUP, SdConfigParamEntity.CRONJOB.CONFIG_KEY_ERROR_EMAIL);
            if(StringUtils.isEmpty(recipient)){
                LOG.warn("Not sending cronjob error email.");
                return;
            }

            // prepare email subject
            LocalDateTime fireDateTime = jobFireTime==null ?
                    null : LocalDateTime.ofInstant(jobFireTime.toInstant(), ZoneId.systemDefault());
            String fireTime = fireDateTime==null ? null : fireDateTime.format(DATE_TIME_FORMATTER);
            String subject = String.format("[Job Error][%s] %s", fireTime, jobName);

            sdEmailService.sendErrorStackTrace(recipient, subject, error);
            LOG.info("Sent job error email.");
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
        }
    }
}
