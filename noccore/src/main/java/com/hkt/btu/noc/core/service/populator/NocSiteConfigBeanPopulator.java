package com.hkt.btu.noc.core.service.populator;


import com.hkt.btu.common.core.service.populator.AbstractBeanPopulator;
import com.hkt.btu.noc.core.service.bean.NocSiteConfigBean;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import java.net.InetAddress;

public class NocSiteConfigBeanPopulator extends AbstractBeanPopulator<NocSiteConfigBean> {


    public void populate(InetAddress inetAddress, NocSiteConfigBean target) {
        if(inetAddress==null){
            return;
        }
        target.setServerHostname(inetAddress.getHostName());
        target.setServerAddress(inetAddress.getHostAddress());
    }

    public void populate(ServletContext servletContext, NocSiteConfigBean target) {
        if(servletContext==null){
            return;
        }
        String contextPath = servletContext.getContextPath();
        if(!StringUtils.isEmpty(contextPath)){
            target.setContextPath(contextPath);
        }

    }


}