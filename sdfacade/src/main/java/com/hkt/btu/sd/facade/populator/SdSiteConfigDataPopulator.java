package com.hkt.btu.sd.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.sd.core.service.bean.SdSiteConfigBean;
import com.hkt.btu.sd.facade.data.SdSiteConfigData;

public class SdSiteConfigDataPopulator extends AbstractDataPopulator<SdSiteConfigData> {

    public void populate(SdSiteConfigBean source, SdSiteConfigData target) {
        target.setGivenDomain(source.getGivenDomain());

        target.setAppName(source.getAppName());
        target.setHttpUrl(source.getAppHttpUrl());
        target.setHttpsUrl(source.getAppHttpsUrl());

        target.setLoginTriedLimit(source.getLoginTriedLimit());
        target.setPasswordLifespanInDay(source.getPasswordLifespanInDay());
        target.setPasswordResetOtpLifespanInMin(source.getPasswordResetOtpLifespanInMin());

        target.setCronjobHostname(source.getCronjobHostname());

        target.setMailHost(source.getMailHost());
        target.setMailPort(source.getMailPort());
        target.setMailUsername(source.getMailUsername());

        target.setContextPath(source.getContextPath());

        target.setServerHostname(source.getServerHostname());
        target.setServerAddress(source.getServerAddress());
        target.setServerType(source.getServerType());
    }
}
