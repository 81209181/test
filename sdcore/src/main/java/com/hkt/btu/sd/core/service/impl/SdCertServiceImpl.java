package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.javax.net.ssl.DummyTrustManager;
import com.hkt.btu.sd.core.service.SdCertService;

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
import java.util.Calendar;

public class SdCertServiceImpl implements SdCertService {

    @Override
    public void checkCert(String url) {
        X509Certificate certificate;
        try {
            HttpsURLConnection connection = getHttpsURLConnection(url);
            Certificate[] certificates = connection.getServerCertificates();
            certificate = (X509Certificate) certificates[0];
        } catch (Exception e) {
            throw new RuntimeException(String.format("Check url[%s] cert fail, error:%s.", url,e.getMessage()));
        }
        long remainingDay = (certificate.getNotAfter().getTime() - Calendar.getInstance().getTimeInMillis()) / (24 * 3600 * 1000);
        if (remainingDay < 90) {
            throw new RuntimeException(String.format("Url:[%s],expiry at %s, %d day(s) left.", url, certificate.getNotAfter(), remainingDay));
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
}
