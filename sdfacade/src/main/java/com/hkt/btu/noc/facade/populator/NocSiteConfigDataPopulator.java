package com.hkt.btu.noc.facade.populator;


import com.hkt.btu.common.facade.populator.AbstractDataPopulator;
import com.hkt.btu.noc.core.service.bean.NocSiteConfigBean;
import com.hkt.btu.noc.facade.data.NocSiteConfigData;

public class NocSiteConfigDataPopulator extends AbstractDataPopulator<NocSiteConfigData> {

    public void populate(NocSiteConfigBean source, NocSiteConfigData target) {
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
