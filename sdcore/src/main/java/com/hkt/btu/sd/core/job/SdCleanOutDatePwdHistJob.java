package com.hkt.btu.sd.core.job;

import com.hkt.btu.sd.core.dao.mapper.SdUserMapper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

@DisallowConcurrentExecution
public class SdCleanOutDatePwdHistJob extends QuartzJobBean {

    @Resource(name = "userMapper")
    SdUserMapper userMapper;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext){
        userMapper.deleteOutDatePwdHistData();
    }
}
