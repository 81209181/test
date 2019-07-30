package com.hkt.btu.noc.core;

import com.hkt.btu.noc.core.dao.entity.NocConfigParamEntity;
import com.hkt.btu.noc.core.service.NocConfigParamService;
import com.hkt.btu.noc.core.service.NocCronJobLogService;
import com.hkt.btu.noc.core.service.NocCronJobProfileService;
import com.hkt.btu.noc.core.service.NocEmailService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.annotation.JacksonFeatures;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Aspect
@Configuration
public class NocJobAspect {
    private static final Logger LOG = LogManager.getLogger(NocJobAspect.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


//    @Resource(name = "cronJobProfileService")
    @Autowired
    NocCronJobProfileService nocCronJobProfileService;
//    @Resource(name = "cronJobLogService")
    @Autowired
    NocCronJobLogService nocCronJobLogService;
//    @Resource(name = "emailService")
    @Autowired
    NocEmailService nocEmailService;
    @Resource(name = "NocConfigParamService")
    NocConfigParamService nocConfigParamService;


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

    @Around("execution(* com.hkt.btu.noc.core.job.*.executeInternal(..))")
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
         *  CronTrigger:    NocSchedulerService.scheduleJob(*).createCronTrigger(*)
         *  SimpleTrigger:  NocSchedulerService.triggerJob(*)
         *  */
        boolean isCronTrigger = jobExecutionContext.getTrigger() instanceof CronTrigger;
        if(isCronTrigger) {
            // check if job should be run
            try {
                boolean isRunnable = nocCronJobProfileService.isRunnable(jobKey.getGroup(), jobKey.getName());
                if (!isRunnable) {
                    LOG.warn("Skipped non-runnable job: " + jobKey);
                    nocCronJobLogService.logSkip(jobDetail);
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
            nocCronJobLogService.logComplete(jobDetail);
            return returnObject;
        } catch (Exception e){
            LOG.error("Cannot complete job: " + jobKey);
            LOG.error(e.getMessage(), e);
            nocCronJobLogService.logError(jobDetail);
            emailJobError(e, jobKey.getName(), jobExecutionContext.getFireTime());
            throw new SchedulerException("Cannot complete job: " + jobKey);
        } finally {
            LOG.info("=========== Ending Job ===========");
        }
    }

    private void emailJobError(Exception error, String jobName, Date jobFireTime){
        try{
            // find recipient
            String recipient = nocConfigParamService.getString(
                    NocConfigParamEntity.CRONJOB.CONFIG_GROUP, NocConfigParamEntity.CRONJOB.CONFIG_KEY_ERROR_EMAIL);
            if(StringUtils.isEmpty(recipient)){
                LOG.warn("Not sending cronjob error email.");
                return;
            }

            // prepare email subject
            LocalDateTime fireDateTime = jobFireTime==null ?
                    null : LocalDateTime.ofInstant(jobFireTime.toInstant(), ZoneId.systemDefault());
            String fireTime = fireDateTime==null ? null : fireDateTime.format(DATE_TIME_FORMATTER);
            String subject = String.format("[Job Error][%s] %s", fireTime, jobName);

            nocEmailService.sendErrorStackTrace(recipient, subject, error);
            LOG.info("Sent job error email.");
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
        }
    }
}
