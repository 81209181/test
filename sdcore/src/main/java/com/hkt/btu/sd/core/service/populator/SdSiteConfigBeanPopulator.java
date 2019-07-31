package com.hkt.btu.sd.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.sd.core.service.bean.SdSiteConfigBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.net.InetAddress;

public class SdSiteConfigBeanPopulator extends AbstractBeanPopulator<SdSiteConfigBean> {


    public void populate(InetAddress inetAddress, SdSiteConfigBean target) {
        if(inetAddress==null){
            return;
        }
        target.setServerHostname(inetAddress.getHostName());
        target.setServerAddress(inetAddress.getHostAddress());
    }

    public void populate(ServletContext servletContext, SdSiteConfigBean target) {
        if(servletContext==null){
            return;
        }
        String contextPath = servletContext.getContextPath();
        if(!StringUtils.isEmpty(contextPath)){
            target.setContextPath(contextPath);
        }

    }


}