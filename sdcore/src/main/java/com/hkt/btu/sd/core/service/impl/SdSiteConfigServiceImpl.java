package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdEmailService;
import com.hkt.btu.sd.core.service.SdSensitiveDataService;
import com.hkt.btu.sd.core.service.SdSiteConfigService;
import com.hkt.btu.sd.core.service.bean.SdSiteConfigBean;
import com.hkt.btu.sd.core.service.populator.SdSiteConfigBeanPopulator;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class SdSiteConfigServiceImpl implements SdSiteConfigService {
    private static final Logger LOG = LogManager.getLogger(SdSiteConfigServiceImpl.class);

    private static SdSiteConfigBean sdSiteConfigBean;

    @Resource(name = "configParamService")
    SdConfigParamService sdConfigParamService;
    @Resource(name = "emailService")
    SdEmailService sdEmailService;
    @Resource(name = "sensitiveDataService")
    SdSensitiveDataService sdSensitiveDataService;

    @Resource(name = "siteConfigBeanPopulator")
    SdSiteConfigBeanPopulator sdSiteConfigBeanPopulator;

    @Resource
    private ServletContext servletContext;

    public void reload(){
        // reload site config bean
        reloadSiteConfigBean();

        // reload related service setting
        sdEmailService.reload();

        // reload key
        sdSensitiveDataService.clearCachedKeys();
    }

    private void reloadSiteConfigBean(){
        SdSiteConfigBean newBean = new SdSiteConfigBean();
        reloadServerInfo(newBean);
        reloadConfigFromDb(newBean);

        // log bean
        LOG.info("SiteConfigBean old: " + sdSiteConfigBean );
        LOG.info("SiteConfigBean new: " + newBean );
        // switch to new bean
        sdSiteConfigBean = newBean;
        LOG.info("SiteConfigBean reloaded.");
    }

    public BtuSiteConfigBean getSiteConfigBean() {
        if (sdSiteConfigBean==null) {
            reload();
        }

        return sdSiteConfigBean;
    }

    public SdSiteConfigBean getSdSiteConfigBean(){
        BtuSiteConfigBean siteConfigBean = getSiteConfigBean();
        if(siteConfigBean instanceof SdSiteConfigBean){
            return (SdSiteConfigBean) siteConfigBean;
        }
        return null;
    }

    @Override
    public boolean isProductionServer() {
        SdSiteConfigBean sdSiteConfigBean = getSdSiteConfigBean();
        return isProductionServer(sdSiteConfigBean);
    }
    private boolean isProductionServer(SdSiteConfigBean sdSiteConfigBean){
        return StringUtils.equals(SdSiteConfigBean.SERVER_TYPE.PROD, sdSiteConfigBean.getServerType()) ||
                StringUtils.equals(SdSiteConfigBean.SERVER_TYPE.PROD_STANDBY, sdSiteConfigBean.getServerType()) ;
    }

    @Override
    public boolean isDevelopmentServer() {
        SdSiteConfigBean sdSiteConfigBean = getSdSiteConfigBean();
        return isDevelopmentServer(sdSiteConfigBean);
    }
    private boolean isDevelopmentServer(SdSiteConfigBean sdSiteConfigBean){
        return StringUtils.equals(SdSiteConfigBean.SERVER_TYPE.DEV, sdSiteConfigBean.getServerType());
    }

    private void reloadConfigFromDb(SdSiteConfigBean siteConfigBean){
        // load config from db
        Map<String, Object> map = sdConfigParamService.getConfigParamByConfigGroup(SdConfigParamEntity.SITE.CONFIG_GROUP);

        String appName = MapUtils.getString(map, SdConfigParamEntity.SITE.CONFIG_KEY_APP_NAME);

        String givenDomain = MapUtils.getString(map, SdConfigParamEntity.SITE.CONFIG_KEY_GIVEN_DOMAIN);

        Integer loginTriedLimit = MapUtils.getInteger(map, SdConfigParamEntity.SITE.CONFIG_KEY_LOGIN_TRIED_LIMIT);
        Integer passwordLifespanInDay = MapUtils.getInteger(map, SdConfigParamEntity.SITE.CONFIG_KEY_PWD_LIFESPAN_IN_DAY);
        Integer passwordResetOtpLifespanInMin = MapUtils.getInteger(map, SdConfigParamEntity.SITE.CONFIG_KEY_PWD_REST_OTP_LIFESPAN_IN_MIN);

        String cronjobHostname = MapUtils.getString(map, SdConfigParamEntity.SITE.CONFIG_KEY_CRONJOB_HOSTNAME);
        String prodHostname = MapUtils.getString(map, SdConfigParamEntity.SITE.CONFIG_KEY_PROD_HOSTNAME);
        String prodStandbyHostname = MapUtils.getString(map, SdConfigParamEntity.SITE.CONFIG_KEY_PROD_STANDBY_HOSTNAME);
        String uatHostname = MapUtils.getString(map, SdConfigParamEntity.SITE.CONFIG_KEY_UAT_HOSTNAME);

        String mailHost = MapUtils.getString(map, SdConfigParamEntity.SITE.CONFIG_KEY_MAIL_HOST);
        Integer mailPort = MapUtils.getInteger(map, SdConfigParamEntity.SITE.CONFIG_KEY_MAIL_PORT);
        String mailUsername = MapUtils.getString(map, SdConfigParamEntity.SITE.CONFIG_KEY_MAIL_USERNAME);

        // set config value from db (if not given may set hardcode value)
        siteConfigBean.setAppName(appName);
        siteConfigBean.setGivenDomain(givenDomain);

        siteConfigBean.setLoginTriedLimit(
                loginTriedLimit==null ? SdSiteConfigBean.DEFAULT_LOGIN_TRIED_LIMIT : loginTriedLimit );
        siteConfigBean.setPasswordLifespanInDay(
                passwordLifespanInDay==null ? SdSiteConfigBean.DEFAULT_PASSWORD_LIFESPAN_IN_DAY : passwordLifespanInDay );
        siteConfigBean.setPasswordResetOtpLifespanInMin(
                passwordResetOtpLifespanInMin==null ? SdSiteConfigBean.DEFAULT_PASSWORD_RESET_OTP_LIFESPAN_IN_MIN : passwordResetOtpLifespanInMin );

        siteConfigBean.setCronjobHostname(
                cronjobHostname==null ? SdSiteConfigBean.DEFAULT_CRONJOB_HOSTNAME : cronjobHostname );

        siteConfigBean.setMailHost(
                mailHost==null ? SdSiteConfigBean.DEFAULT_MAIL_HOST : mailHost );
        siteConfigBean.setMailPort(
                mailPort==null ? SdSiteConfigBean.DEFAULT_MAIL_PORT : mailPort );
        siteConfigBean.setMailUsername(
                mailUsername==null ? SdSiteConfigBean.DEFAULT_MAIL_USERNAME : mailUsername );// formatted var

        // determine server type
        String serverHostname = siteConfigBean.getServerHostname();
        if( StringUtils.equals(serverHostname, prodHostname) ){
            siteConfigBean.setServerType(SdSiteConfigBean.SERVER_TYPE.PROD);
        } else if ( StringUtils.equals(serverHostname, prodStandbyHostname) ){
            siteConfigBean.setServerType(SdSiteConfigBean.SERVER_TYPE.PROD_STANDBY);
        } else if ( StringUtils.equals(serverHostname, uatHostname) ){
            siteConfigBean.setServerType(SdSiteConfigBean.SERVER_TYPE.UAT);
        } else {
            siteConfigBean.setServerType(SdSiteConfigBean.SERVER_TYPE.DEV);
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

    private void reloadServerInfo(SdSiteConfigBean siteConfigBean) {
        InetAddress inetAddress = getServerInetAddress();
        sdSiteConfigBeanPopulator.populate(inetAddress, siteConfigBean);
        sdSiteConfigBeanPopulator.populate(servletContext, siteConfigBean);
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
