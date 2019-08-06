package com.hkt.btu.common.core.service.populator;

import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import java.net.InetAddress;

public class BtuSiteConfigBeanPopulator {

    public void populate(InetAddress inetAddress, BtuSiteConfigBean target) {
        if (inetAddress == null) {
            return;
        }
        target.setServerHostname(inetAddress.getHostName());
        target.setServerAddress(inetAddress.getHostAddress());
    }

    public void populate(ServletContext servletContext, BtuSiteConfigBean target) {
        if (servletContext == null) {
            return;
        }
        String contextPath = servletContext.getContextPath();
        if (!StringUtils.isEmpty(contextPath)) {
            target.setContextPath(contextPath);
        }

    }


}
