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
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class BtuSiteConfigServiceImpl implements BtuSiteConfigService {
    private static final Logger LOG = LogManager.getLogger(BtuSiteConfigService.class);

    private static BtuSiteConfigBean btuSiteConfigBean;

    @Resource(name = "siteConfigBeanPopulator")
    BtuSiteConfigBeanPopulator siteConfigBeanPopulator;

    @Resource(name = "configParamService")
    BtuConfigParamService btuConfigParamService;

    @Resource(name = "emailService")
    BtuEmailService btuEmailService;

    @Resource(name = "sensitiveDataService")
    BtuSensitiveDataService btuSensitiveDataService;

    @Resource
    private ServletContext servletContext;

    @Override
    public BtuSiteConfigBean getSiteConfigBean() {
        if (ObjectUtils.isEmpty(btuSiteConfigBean)) {
            reload();
        }
        return btuSiteConfigBean;
    }

    @Override
    public void reload() {
        // reload site config bean
        reloadSiteConfigBean();

        // reload related service setting
        btuEmailService.reload();

        // reload key
        btuSensitiveDataService.clearCachedKeys();
    }

    @Override
    public boolean isProductionServer() {
        BtuSiteConfigBean btuSiteConfigBean = getSiteConfigBean();
        return StringUtils.equals(BtuSiteConfigBean.SERVER_TYPE.PROD, btuSiteConfigBean.getServerType()) ||
                StringUtils.equals(BtuSiteConfigBean.SERVER_TYPE.PROD_STANDBY, btuSiteConfigBean.getServerType()) ;
    }

    @Override
    public boolean isDevelopmentServer() {
        BtuSiteConfigBean btuSiteConfigBean = getSiteConfigBean();
        return StringUtils.equals(BtuSiteConfigBean.SERVER_TYPE.DEV, btuSiteConfigBean.getServerType());
    }

    private void reloadSiteConfigBean() {
        BtuSiteConfigBean newBean = new BtuSiteConfigBean();
        reloadServerInfo(newBean);
        reloadConfigFromDb(newBean);

        // log bean
        LOG.info("SiteConfigBean old: " + btuSiteConfigBean);
        LOG.info("SiteConfigBean new: " + newBean);
        // switch to new bean
        btuSiteConfigBean = newBean;
        LOG.info("SiteConfigBean reloaded.");

    }

    private void reloadConfigFromDb(BtuSiteConfigBean siteConfigBean) {
        // load config from db
        Map<String, Object> map = btuConfigParamService.getConfigParamByConfigGroup(BtuConfigParamEntity.SITE.CONFIG_GROUP, false);

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
        siteConfigBean.setAppName(appName);
        siteConfigBean.setGivenDomain(givenDomain);

        siteConfigBean.setLoginTriedLimit(
                loginTriedLimit==null ? BtuSiteConfigBean.DEFAULT_LOGIN_TRIED_LIMIT : loginTriedLimit );
        siteConfigBean.setPasswordLifespanInDay(
                passwordLifespanInDay==null ? BtuSiteConfigBean.DEFAULT_PASSWORD_LIFESPAN_IN_DAY : passwordLifespanInDay );
        siteConfigBean.setPasswordResetOtpLifespanInMin(
                passwordResetOtpLifespanInMin==null ? BtuSiteConfigBean.DEFAULT_PASSWORD_RESET_OTP_LIFESPAN_IN_MIN : passwordResetOtpLifespanInMin );

        siteConfigBean.setCronjobHostname(
                cronjobHostname==null ? BtuSiteConfigBean.DEFAULT_CRONJOB_HOSTNAME : cronjobHostname );

        siteConfigBean.setMailHost(
                mailHost==null ? BtuSiteConfigBean.DEFAULT_MAIL_HOST : mailHost );
        siteConfigBean.setMailPort(
                mailPort==null ? BtuSiteConfigBean.DEFAULT_MAIL_PORT : mailPort );
        siteConfigBean.setMailUsername(
                mailUsername==null ? BtuSiteConfigBean.DEFAULT_MAIL_USERNAME : mailUsername );// formatted var

        // determine server type
        String serverHostname = siteConfigBean.getServerHostname();
        if( StringUtils.equals(serverHostname, prodHostname) ){
            siteConfigBean.setServerType(BtuSiteConfigBean.SERVER_TYPE.PROD);
        } else if ( StringUtils.equals(serverHostname, prodStandbyHostname) ){
            siteConfigBean.setServerType(BtuSiteConfigBean.SERVER_TYPE.PROD_STANDBY);
        } else if ( StringUtils.equals(serverHostname, uatHostname) ){
            siteConfigBean.setServerType(BtuSiteConfigBean.SERVER_TYPE.UAT);
        } else {
            siteConfigBean.setServerType(BtuSiteConfigBean.SERVER_TYPE.DEV);
        }

        // determine base url
        if(isDevelopmentServer(siteConfigBean)){
            siteConfigBean.setAppHttpUrl( String.format("http://%s%s", "localhost:8080", siteConfigBean.getContextPath()) );
            siteConfigBean.setAppHttpsUrl( String.format("https://%s%s", "localhost:8080", siteConfigBean.getContextPath()) );
        }else {
            siteConfigBean.setAppHttpUrl( String.format("http://%s%s", siteConfigBean.getGivenDomain(), siteConfigBean.getContextPath()) );
            siteConfigBean.setAppHttpsUrl( String.format("https://%s%s", siteConfigBean.getGivenDomain(), siteConfigBean.getContextPath()) );
        }

    }

    private boolean isDevelopmentServer(BtuSiteConfigBean siteConfigBean) {
        return StringUtils.equals(BtuSiteConfigBean.SERVER_TYPE.DEV, siteConfigBean.getServerType());
    }

    private void reloadServerInfo(BtuSiteConfigBean siteConfigBean) {
        InetAddress inetAddress = getServerInetAddress();
        siteConfigBeanPopulator.populate(inetAddress, siteConfigBean);
        siteConfigBeanPopulator.populate(servletContext, siteConfigBean);
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
