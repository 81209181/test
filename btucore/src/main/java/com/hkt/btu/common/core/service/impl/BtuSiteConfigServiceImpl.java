package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuConfigParamService;
import com.hkt.btu.common.core.service.BtuEmailService;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.service.populator.BtuSiteConfigBeanPopulator;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class BtuSiteConfigServiceImpl implements BtuSiteConfigService {
    private static final Logger LOG = LogManager.getLogger(BtuSiteConfigService.class);

    private static BtuSiteConfigBean siteConfigBean;

    @Resource(name = "configParamService")
    BtuConfigParamService configParamService;
    @Resource(name = "emailService")
    BtuEmailService emailService;
    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService sensitiveDataService;

    @Resource(name = "siteConfigBeanPopulator")
    BtuSiteConfigBeanPopulator sdSiteConfigBeanPopulator;

    @Resource
    private ServletContext servletContext;

    public BtuSiteConfigBean getSiteConfigBean() {
        if (siteConfigBean == null) {
            reload();
        }
        return siteConfigBean;
    }

    @Override
    public boolean isProductionServer() {
        BtuSiteConfigBean siteConfigBean = getSiteConfigBean();
        return isProductionServer(siteConfigBean);
    }

    private boolean isProductionServer(BtuSiteConfigBean siteConfigBean) {
        return StringUtils.equals(BtuSiteConfigBean.SERVER_TYPE.PROD, siteConfigBean.getServerType()) ||
                StringUtils.equals(BtuSiteConfigBean.SERVER_TYPE.PROD_STANDBY, siteConfigBean.getServerType()) ;

    }

    @Override
    public boolean isDevelopmentServer() {
        BtuSiteConfigBean sdSiteConfigBean = getSiteConfigBean();
        return isDevelopmentServer(sdSiteConfigBean);
    }

    @Override
    public void reload() {
        // reload site config bean
        reloadSiteConfigBean();

        // reload related service setting
        emailService.reload();

        // reload key
        sensitiveDataService.clearCachedKeys();

    }

    private void reloadSiteConfigBean() {
        BtuSiteConfigBean newBean = new BtuSiteConfigBean();
        reloadServerInfo(newBean);
        reloadConfigFromDb(newBean);

        // log bean
        LOG.info("SiteConfigBean old: " + siteConfigBean);
        LOG.info("SiteConfigBean new: " + newBean);
        // switch to new bean
        siteConfigBean = newBean;
        LOG.info("SiteConfigBean reloaded.");

    }

    private void reloadConfigFromDb(BtuSiteConfigBean configBean) {
        // load config from db
        Map<String, Object> map = configParamService.getConfigParamByConfigGroup(BtuConfigParamEntity.SITE.CONFIG_GROUP);

        String appName = MapUtils.getString(map, BtuConfigParamEntity.SITE.CONFIG_KEY_APP_NAME);

        String givenDomain = MapUtils.getString(map, BtuConfigParamEntity.SITE.CONFIG_KEY_GIVEN_DOMAIN);

        Integer loginTriedLimit = MapUtils.getInteger(map, BtuConfigParamEntity.SITE.CONFIG_KEY_LOGIN_TRIED_LIMIT);
        Integer passwordLifespanInDay = MapUtils.getInteger(map, BtuConfigParamEntity.SITE.CONFIG_KEY_PWD_LIFESPAN_IN_DAY);
        Integer passwordResetOtpLifespanInMin = MapUtils.getInteger(map, BtuConfigParamEntity.SITE.CONFIG_KEY_PWD_REST_OTP_LIFESPAN_IN_MIN);

        String cronjobHostname = MapUtils.getString(map, BtuConfigParamEntity.SITE.CONFIG_KEY_CRONJOB_HOSTNAME);
        String prodHostname = MapUtils.getString(map, BtuConfigParamEntity.SITE.CONFIG_KEY_PROD_HOSTNAME);
        String prodStandbyHostname = MapUtils.getString(map, BtuConfigParamEntity.SITE.CONFIG_KEY_PROD_STANDBY_HOSTNAME);
        String uatHostname = MapUtils.getString(map, BtuConfigParamEntity.SITE.CONFIG_KEY_UAT_HOSTNAME);

        String mailHost = MapUtils.getString(map, BtuConfigParamEntity.SITE.CONFIG_KEY_MAIL_HOST);
        Integer mailPort = MapUtils.getInteger(map, BtuConfigParamEntity.SITE.CONFIG_KEY_MAIL_PORT);
        String mailUsername = MapUtils.getString(map, BtuConfigParamEntity.SITE.CONFIG_KEY_MAIL_USERNAME);

        // set config value from db (if not given may set hardcode value)
        configBean.setAppName(appName);
        configBean.setGivenDomain(givenDomain);

        configBean.setLoginTriedLimit(
                loginTriedLimit == null ? BtuSiteConfigBean.DEFAULT_LOGIN_TRIED_LIMIT : loginTriedLimit);
        configBean.setPasswordLifespanInDay(
                passwordLifespanInDay == null ? BtuSiteConfigBean.DEFAULT_PASSWORD_LIFESPAN_IN_DAY : passwordLifespanInDay);
        configBean.setPasswordResetOtpLifespanInMin(
                passwordResetOtpLifespanInMin == null ? BtuSiteConfigBean.DEFAULT_PASSWORD_RESET_OTP_LIFESPAN_IN_MIN : passwordResetOtpLifespanInMin);

        configBean.setCronjobHostname(
                cronjobHostname == null ? BtuSiteConfigBean.DEFAULT_CRONJOB_HOSTNAME : cronjobHostname);

        configBean.setMailHost(
                mailHost == null ? BtuSiteConfigBean.DEFAULT_MAIL_HOST : mailHost);
        configBean.setMailPort(
                mailPort == null ? BtuSiteConfigBean.DEFAULT_MAIL_PORT : mailPort);
        configBean.setMailUsername(
                mailUsername == null ? BtuSiteConfigBean.DEFAULT_MAIL_USERNAME : mailUsername);// formatted var

        // determine server type
        String serverHostname = configBean.getServerHostname();
        if (StringUtils.equals(serverHostname, prodHostname)) {
            configBean.setServerType(BtuSiteConfigBean.SERVER_TYPE.PROD);
        } else if (StringUtils.equals(serverHostname, prodStandbyHostname)) {
            configBean.setServerType(BtuSiteConfigBean.SERVER_TYPE.PROD_STANDBY);
        } else if (StringUtils.equals(serverHostname, uatHostname)) {
            configBean.setServerType(BtuSiteConfigBean.SERVER_TYPE.UAT);
        } else {
            configBean.setServerType(BtuSiteConfigBean.SERVER_TYPE.DEV);
        }

        // determine base url
        if (isDevelopmentServer(configBean)) {
            configBean.setAppHttpUrl(String.format("http://%s%s", "localhost:8080", configBean.getContextPath()));
            configBean.setAppHttpsUrl(String.format("https://%s%s", "localhost:8080", configBean.getContextPath()));
        } else {
            configBean.setAppHttpUrl(String.format("http://%s%s", configBean.getGivenDomain(), configBean.getContextPath()));
            configBean.setAppHttpsUrl(String.format("https://%s%s", configBean.getGivenDomain(), configBean.getContextPath()));
        }

    }

    private boolean isDevelopmentServer(BtuSiteConfigBean configBean) {
        return StringUtils.equals(BtuSiteConfigBean.SERVER_TYPE.DEV, configBean.getServerType());
    }

    private void reloadServerInfo(BtuSiteConfigBean configBean) {
        InetAddress inetAddress = getServerInetAddress();
        sdSiteConfigBeanPopulator.populate(inetAddress, configBean);
        sdSiteConfigBeanPopulator.populate(servletContext, configBean);

    }

    private InetAddress getServerInetAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}
