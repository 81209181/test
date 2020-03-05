package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.common.javax.net.ssl.DummyTrustManager;
import com.hkt.btu.sd.core.service.SdCertService;
import com.hkt.btu.sd.core.service.bean.SdCheckCertBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SdCertServiceImpl implements SdCertService {
    private static final Logger LOG = LogManager.getLogger(SdCertServiceImpl.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SdCheckCertBean checkCert(String path) {
        final LocalDate TODAY = LocalDate.now();

        LOG.info(String.format("Checking cert of [%s]...", path));

        try {
            // get connection, trust connection of path
            HttpsURLConnection connection = getHttpsURLConnection(path);

            // get certificates
            Certificate[] serverCertificates = connection.getServerCertificates();
            Certificate certificate = serverCertificates == null || serverCertificates.length == 0 ? null : serverCertificates[0];
            X509Certificate x509Certificate = (certificate instanceof X509Certificate) ? (X509Certificate) certificate : null;
            if (x509Certificate == null) {
                String feedback = String.format("Cannot get (x509) certificate from [%s].", path);
                LOG.info(feedback);
                return new SdCheckCertBean(path, null, 0, feedback);
            }

            // get not after date
            Date certNotAfterDate = x509Certificate.getNotAfter();
            LocalDateTime notAfterDateTime = certNotAfterDate == null ?
                    null : LocalDateTime.ofInstant(certNotAfterDate.toInstant(), ZoneId.systemDefault());
            if (notAfterDateTime == null) {
                String feedback = String.format("Cannot get certificate not after date from [%s].", path);
                LOG.info(feedback);
                return new SdCheckCertBean(path, null, 0, feedback);
            }

            // count days before expiry
            long remainingDays = TODAY.until(notAfterDateTime, ChronoUnit.DAYS);

            // return checking result
            return new SdCheckCertBean(path, notAfterDateTime, remainingDays, null);
        } catch (Exception e){
            String errorMsg = String.format("Cannot check cert of %s. (%s)", path, e.getMessage());
            LOG.error(errorMsg);
            LOG.error(e.getMessage(), e);
            return new SdCheckCertBean(path, null, 0, errorMsg);
        }
    }

    private HttpsURLConnection getHttpsURLConnection(String url) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(3000);
        TrustManager[] tm = {new DummyTrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tm, new java.security.SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        connection.setSSLSocketFactory(ssf);
        connection.connect();
        return connection;
    }

    @Override
    public List<SdCheckCertBean> checkCert(List<BtuConfigParamBean> configParamBeanList) {
        if(CollectionUtils.isEmpty(configParamBeanList)){
            LOG.warn("No url found for checking cert.");
            return null;
        }

        List<SdCheckCertBean> checkCertBeanList = new ArrayList<>();
        for (BtuConfigParamBean configParamBean : configParamBeanList) {
            String path = configParamBean.getConfigValue();
            SdCheckCertBean checkCertBean = checkCert(path);
            checkCertBeanList.add(checkCertBean);
        }
        return checkCertBeanList;
    }

    @Override
    public String formEmailBody(List<SdCheckCertBean> checkCertBeanList) {
        if(CollectionUtils.isEmpty(checkCertBeanList)){
            return "No url found for checking cert.";
        }

        StringBuilder feedbackBuilder = new StringBuilder();
        for(SdCheckCertBean checkCertBean : checkCertBeanList){
            String url = checkCertBean.getUrl();
            String errorMsg = checkCertBean.getErrorMsg();
            String expiryDate = checkCertBean.getExpiryDate()==null ?
                    "unknown" : checkCertBean.getExpiryDate().format(DATE_TIME_FORMATTER);
            long remainingDays = checkCertBean.getRemainingDays();

            String plaintextFeedback = String.format(
                    "Url:[%s], expires at %s. %d day(s) left.", url, expiryDate, remainingDays);
            if(StringUtils.isNotEmpty(errorMsg)){
                plaintextFeedback += " (error: " + errorMsg + ")";
            }

            final long MIN_REMAINING_DAYS = 60;
            String htmlFeedback;
            if( remainingDays < MIN_REMAINING_DAYS || StringUtils.isNotEmpty(errorMsg) ){
                // alerting
                LOG.warn(plaintextFeedback);
                htmlFeedback = String.format("<span style='color:red'>%s</span><br/>", plaintextFeedback);
            } else {
                // normal
                LOG.info(plaintextFeedback);
                htmlFeedback = String.format("<span style='color:green'>%s</span><br/>", plaintextFeedback);
            }

            feedbackBuilder.append( htmlFeedback );
        }

        return feedbackBuilder.toString();
    }
}
