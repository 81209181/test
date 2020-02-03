package com.hkt.btu.sd.core.service.impl;


import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuCronJobProfileBean;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.service.impl.BtuCronJobLogServiceImpl;
import com.hkt.btu.sd.core.dao.mapper.SdCronJobLogMapper;
import com.hkt.btu.sd.core.service.SdCronJobLogService;
import com.hkt.btu.sd.core.service.SdCronJobProfileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

public class SdCronJobLogServiceImpl extends BtuCronJobLogServiceImpl implements SdCronJobLogService {
    private static final Logger LOG = LogManager.getLogger(SdCronJobLogServiceImpl.class);

    @Resource
    SdCronJobLogMapper sdCronJobLogMapper;

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;


    @Override
    protected void addJobLogInternal(String jobGroup, String jobName, String jobClass, String createby, String action) {
        // get server info
        BtuSiteConfigBean btuSiteConfigBean = siteConfigService.getSiteConfigBean();
        String serverHostname = btuSiteConfigBean.getServerHostname();
        String serverIp = btuSiteConfigBean.getServerAddress();

        // insert
        LOG.info("Log Job change: {}, {}, {}, {}, {}, {}",
                serverHostname, jobGroup, jobName, jobClass, action, createby);
        sdCronJobLogMapper.insertLog(serverHostname, serverIp, jobGroup, jobName, jobClass, action, createby);
    }

}
