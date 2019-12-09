package com.hkt.btu.sd.facade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.hkt.btu.common.facade.data.DataInterface;
import com.hkt.btu.sd.core.service.SdSiteService;
import com.hkt.btu.sd.core.service.bean.SiteInterfaceBean;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientResponse;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AbstractRestfulApiFacade {

    private static final Logger LOG = LogManager.getLogger(AbstractRestfulApiFacade.class);

    @Resource(name = "siteService")
    SdSiteService siteService;

    private Gson gson;

    // API dependent methods
    protected abstract SiteInterfaceBean getTargetApiSiteInterfaceBean();

    protected abstract Invocation.Builder getInvocationBuilder(WebTarget webTarget);

    protected String getBtuHeaderAuthKey(SiteInterfaceBean siteInterfaceBean) {
        String authPlainText = String.format("%s:%s", siteInterfaceBean.getUserName(), siteInterfaceBean.getPassword());
        String encodedAuth = Base64.getEncoder().encodeToString(authPlainText.getBytes());
        return String.format("Basic %s", encodedAuth);
    }

    private WebTarget getWebTarget(String path, Map<String, String> queryParamMap) {
        SiteInterfaceBean siteInterfaceBean = getTargetApiSiteInterfaceBean();
        if (siteInterfaceBean == null) {
            LOG.error("API config not found.");
            return null;
        }

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        if (!siteService.isProductionServer()) {
            // ignore SSL validation in non-Production environment
            SSLContext sslcontext;
            try {
                // ref: https://stackoverflow.com/questions/6047996/ignore-self-signed-ssl-cert-using-jersey-client
                sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                    }

                    public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }}, new java.security.SecureRandom());

                LOG.warn("Bypassed SSL validation for Restful API.");
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                LOG.error("Cannot bypass SSL validation.");
                LOG.error(e.getMessage(), e);
                return null;
            }

            clientBuilder = clientBuilder
                    .hostnameVerifier((s1, s2) -> true)
                    .sslContext(sslcontext);
        }
        Client client = clientBuilder.build();

        // set api path
        WebTarget webTarget = client
                .target(siteInterfaceBean.getUrl())
                .path(path);

        // add query parameter
        if (!MapUtils.isEmpty(queryParamMap)) {
            for (String key : queryParamMap.keySet()) {
                String value = queryParamMap.get(key);
                if (value == null) {
                    continue;
                }

                webTarget = webTarget.queryParam(key, value);
            }
        }

        return webTarget;
    }

    protected String getData(String path, Map<String, String> queryParamMap) {
        LOG.info("Getting from API: " + path);

        WebTarget webTarget = getWebTarget(path, queryParamMap);
        Invocation.Builder invocationBuilder = getInvocationBuilder(webTarget);
        try {
            return invocationBuilder.get(String.class);
        } catch (ProcessingException | WebApplicationException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    protected <T extends DataInterface> T getData(String path, Class<T> responseType, Map<String, String> queryParamMap) {
        String jsonString = getData(path, queryParamMap);
        try {
            // using gson here with purpose for easier debugging (better exception for stack trace)
            if (gson == null) {
                gson = getGson();
            }
            return gson.fromJson(jsonString, responseType);
        } catch (ProcessingException | WebApplicationException e) {
            LOG.error(e.getMessage(), e);
            LOG.debug(jsonString);
            return null;
        }
    }

    protected <T extends DataInterface> List<T> getDataList(String path, Type type, Map<String, String> queryParamMap) {
        String jsonString = getData(path, queryParamMap);
        try {
            // using gson here with purpose for easier debugging (better exception for stack trace)
            if (gson == null) {
                gson = getGson();
            }

            return gson.fromJson(jsonString, type);
        } catch (ProcessingException | WebApplicationException e) {
            LOG.error(e.getMessage(), e);
            LOG.debug(jsonString);
            return null;
        }
    }
    protected Response postEntity(String url,Entity<?> entity){
        WebTarget webTarget = getWebTarget(url, null);
        Invocation.Builder invocationBuilder = getInvocationBuilder(webTarget);
        return invocationBuilder.post(entity);
    }

    protected String postData(String path, Map<String, String> queryParamMap, Entity<?> entity) {
        LOG.info("Posting to API: " + path);

        WebTarget webTarget = getWebTarget(path, queryParamMap);
        Invocation.Builder invocationBuilder = getInvocationBuilder(webTarget);
        try {
            return invocationBuilder.post(entity, String.class);
        } catch (ProcessingException | WebApplicationException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    protected <T extends DataInterface> T postData(String path, Class<T> responseType, Map<String, String> queryParamMap, Entity<?> entity) {
        String jsonString = postData(path, queryParamMap, entity);
        try {
            // using gson here with purpose for easier debugging (better exception for stack trace)
            return new Gson().fromJson(jsonString, responseType);
        } catch (ProcessingException | WebApplicationException e) {
            LOG.error(e.getMessage(), e);
            LOG.debug(jsonString);
            return null;
        }
    }

    protected String putData(String path, Map<String, String> queryParamMap, Entity<?> entity) {
        LOG.info("Posting to API: " + path);
        if(entity==null){
            entity = Entity.text(StringUtils.EMPTY);
        }

        WebTarget webTarget = getWebTarget(path, queryParamMap);
        Invocation.Builder invocationBuilder = getInvocationBuilder(webTarget);
        try {
            return invocationBuilder.put(entity, String.class);
        } catch (ProcessingException | WebApplicationException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    protected <T extends DataInterface> T putData(String path, Class<T> responseType, Map<String, String> queryParamMap, Entity<?> entity) {
        String jsonString = putData(path, queryParamMap, entity);
        try {
            // using gson here with purpose for easier debugging (better exception for stack trace)
            return new Gson().fromJson(jsonString, responseType);
        } catch (ProcessingException | WebApplicationException e) {
            LOG.error(e.getMessage(), e);
            LOG.debug(jsonString);
            return null;
        }
    }

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> getDate(json));
        return builder.create();
    }

    private Date getDate(JsonElement json) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dfString = format.format(json.getAsJsonPrimitive().getAsLong());
        try {
            return format.parse(dfString);
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}
