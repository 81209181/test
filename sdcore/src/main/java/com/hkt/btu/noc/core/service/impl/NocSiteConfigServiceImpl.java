package com.hkt.btu.noc.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.noc.core.dao.entity.NocConfigParamEntity;
import com.hkt.btu.noc.core.service.NocConfigParamService;
import com.hkt.btu.noc.core.service.NocEmailService;
import com.hkt.btu.noc.core.service.NocSensitiveDataService;
import com.hkt.btu.noc.core.service.NocSiteConfigService;
import com.hkt.btu.noc.core.service.bean.NocSiteConfigBean;
import com.hkt.btu.noc.core.service.populator.NocSiteConfigBeanPopulator;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class NocSiteConfigServiceImpl implements NocSiteConfigService {
    private static final Logger LOG = LogManager.getLogger(NocSiteConfigServiceImpl.class);

    private static NocSiteConfigBean nocSiteConfigBean;

    @Resource(name = "configParamService")
    NocConfigParamService nocConfigParamService;
    @Resource(name = "emailService")
    NocEmailService nocEmailService;
    @Resource(name = "sensitiveDataService")
    NocSensitiveDataService nocSensitiveDataService;

    @Resource(name = "siteConfigBeanPopulator")
    NocSiteConfigBeanPopulator nocSiteConfigBeanPopulator;

    @Resource
    private ServletContext servletContext;

    public void reload(){
        // reload site config bean
        reloadSiteConfigBean();

        // reload related service setting
        nocEmailService.reload();

        // reload key
        nocSensitiveDataService.clearCachedKeys();
    }

    private void reloadSiteConfigBean(){
        NocSiteConfigBean newBean = new NocSiteConfigBean();
        reloadServerInfo(newBean);
        reloadConfigFromDb(newBean);

        // log bean
        LOG.info("SiteConfigBean old: " + nocSiteConfigBean );
        LOG.info("SiteConfigBean new: " + newBean );
        // switch to new bean
        nocSiteConfigBean = newBean;
        LOG.info("SiteConfigBean reloaded.");
    }

    public BtuSiteConfigBean getSiteConfigBean() {
        if (nocSiteConfigBean==null) {
            reload();
        }

        return nocSiteConfigBean;
    }

    public NocSiteConfigBean getNocSiteConfigBean(){
        BtuSiteConfigBean siteConfigBean = getSiteConfigBean();
        if(siteConfigBean instanceof NocSiteConfigBean){
            return (NocSiteConfigBean) siteConfigBean;
        }
        return null;
    }

    @Override
    public boolean isProductionServer() {
        NocSiteConfigBean nocSiteConfigBean = getNocSiteConfigBean();
        return isProductionServer(nocSiteConfigBean);
    }
    private boolean isProductionServer(NocSiteConfigBean nocSiteConfigBean){
        return StringUtils.equals(NocSiteConfigBean.SERVER_TYPE.PROD, nocSiteConfigBean.getServerType()) ||
                StringUtils.equals(NocSiteConfigBean.SERVER_TYPE.PROD_STANDBY, nocSiteConfigBean.getServerType()) ;
    }

    @Override
    public boolean isDevelopmentServer() {
        NocSiteConfigBean nocSiteConfigBean = getNocSiteConfigBean();
        return isDevelopmentServer(nocSiteConfigBean);
    }
    private boolean isDevelopmentServer(NocSiteConfigBean nocSiteConfigBean){
        return StringUtils.equals(NocSiteConfigBean.SERVER_TYPE.DEV, nocSiteConfigBean.getServerType());
    }

    private void reloadConfigFromDb(NocSiteConfigBean siteConfigBean){
        // load config from db
        Map<String, Object> map = nocConfigParamService.getConfigParamByConfigGroup(NocConfigParamEntity.SITE.CONFIG_GROUP);

        String appName = MapUtils.getString(map, NocConfigParamEntity.SITE.CONFIG_KEY_APP_NAME);

        String givenDomain = MapUtils.getString(map, NocConfigParamEntity.SITE.CONFIG_KEY_GIVEN_DOMAIN);

        Integer loginTriedLimit = MapUtils.getInteger(map, NocConfigParamEntity.SITE.CONFIG_KEY_LOGIN_TRIED_LIMIT);
        Integer passwordLifespanInDay = MapUtils.getInteger(map, NocConfigParamEntity.SITE.CONFIG_KEY_PWD_LIFESPAN_IN_DAY);
        Integer passwordResetOtpLifespanInMin = MapUtils.getInteger(map, NocConfigParamEntity.SITE.CONFIG_KEY_PWD_REST_OTP_LIFESPAN_IN_MIN);

        String cronjobHostname = MapUtils.getString(map, NocConfigParamEntity.SITE.CONFIG_KEY_CRONJOB_HOSTNAME);
        String prodHostname = MapUtils.getString(map, NocConfigParamEntity.SITE.CONFIG_KEY_PROD_HOSTNAME);
        String prodStandbyHostname = MapUtils.getString(map, NocConfigParamEntity.SITE.CONFIG_KEY_PROD_STANDBY_HOSTNAME);
        String uatHostname = MapUtils.getString(map, NocConfigParamEntity.SITE.CONFIG_KEY_UAT_HOSTNAME);

        String mailHost = MapUtils.getString(map, NocConfigParamEntity.SITE.CONFIG_KEY_MAIL_HOST);
        Integer mailPort = MapUtils.getInteger(map, NocConfigParamEntity.SITE.CONFIG_KEY_MAIL_PORT);
        String mailUsername = MapUtils.getString(map, NocConfigParamEntity.SITE.CONFIG_KEY_MAIL_USERNAME);

        // set config value from db (if not given may set hardcode value)
        siteConfigBean.setAppName(appName);
        siteConfigBean.setGivenDomain(givenDomain);

        siteConfigBean.setLoginTriedLimit(
                loginTriedLimit==null ? NocSiteConfigBean.DEFAULT_LOGIN_TRIED_LIMIT : loginTriedLimit );
        siteConfigBean.setPasswordLifespanInDay(
                passwordLifespanInDay==null ? NocSiteConfigBean.DEFAULT_PASSWORD_LIFESPAN_IN_DAY : passwordLifespanInDay );
        siteConfigBean.setPasswordResetOtpLifespanInMin(
                passwordResetOtpLifespanInMin==null ? NocSiteConfigBean.DEFAULT_PASSWORD_RESET_OTP_LIFESPAN_IN_MIN : passwordResetOtpLifespanInMin );

        siteConfigBean.setCronjobHostname(
                cronjobHostname==null ? NocSiteConfigBean.DEFAULT_CRONJOB_HOSTNAME : cronjobHostname );

        siteConfigBean.setMailHost(
                mailHost==null ? NocSiteConfigBean.DEFAULT_MAIL_HOST : mailHost );
        siteConfigBean.setMailPort(
                mailPort==null ? NocSiteConfigBean.DEFAULT_MAIL_PORT : mailPort );
        siteConfigBean.setMailUsername(
                mailUsername==null ? NocSiteConfigBean.DEFAULT_MAIL_USERNAME : mailUsername );// formatted var

        // determine server type
        String serverHostname = siteConfigBean.getServerHostname();
        if( StringUtils.equals(serverHostname, prodHostname) ){
            siteConfigBean.setServerType(NocSiteConfigBean.SERVER_TYPE.PROD);
        } else if ( StringUtils.equals(serverHostname, prodStandbyHostname) ){
            siteConfigBean.setServerType(NocSiteConfigBean.SERVER_TYPE.PROD_STANDBY);
        } else if ( StringUtils.equals(serverHostname, uatHostname) ){
            siteConfigBean.setServerType(NocSiteConfigBean.SERVER_TYPE.UAT);
        } else {
            siteConfigBean.setServerType(NocSiteConfigBean.SERVER_TYPE.DEV);
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

    private void reloadServerInfo(NocSiteConfigBean siteConfigBean) {
        InetAddress inetAddress = getServerInetAddress();
        nocSiteConfigBeanPopulator.populate(inetAddress, siteConfigBean);
        nocSiteConfigBeanPopulator.populate(servletContext, siteConfigBean);
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
