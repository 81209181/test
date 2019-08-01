package com.hkt.btu.common.spring.security.access.intercept;


import com.hkt.btu.common.core.service.BtuPathCtrlService;
import com.hkt.btu.common.core.service.bean.BtuUserGroupPathCtrlBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.PostConstruct;
import java.util.*;


public class BtuSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger LOG = LogManager.getLogger(BtuSecurityMetadataSource.class);

    // cached Secured URLs config
    private static Map<RequestMatcher, Collection<ConfigAttribute>> resourceMap = null;

    // default user group
    public static final ConfigAttribute CONFIG_ATTR_PERMIT_ALL = new SecurityConfig("permitAll");
    public static final ConfigAttribute CONFIG_ATTR_DENY_ALL = new SecurityConfig("denyAll");
    private static final List<ConfigAttribute> CONFIG_LIST_PERMIT_ALL = Collections.singletonList(CONFIG_ATTR_PERMIT_ALL);
    private static final List<ConfigAttribute> CONFIG_LIST_DENY_ALL = Collections.singletonList(CONFIG_ATTR_DENY_ALL);

    // reserved for public access
    public static final String RESERVED_ANT_PATH_ROOT = "/";
    public static final String RESERVED_ANT_PATH_LOGIN = "/login*";
    public static final String RESERVED_ANT_PATH_RESET_PWD = "/reset-password*";
    public static final String RESERVED_ANT_PATH_RESET_PWD_OTP = "/reset-password-otp*";
    public static final String RESERVED_ANT_PATH_JS = "/js/**";
    public static final String RESERVED_ANT_PATH_CSS = "/css/**";
    public static final String RESERVED_ANT_PATH_LIB = "/DataTables/**";
    public static final String RESERVED_ANT_PATH_IMG = "/img/**";
    public static final String RESERVED_ANT_PATH_WEBJAR = "/webjars/**";
    public static final String RESERVED_ANT_PATH_ERROR = "/error/**";
    public static final String RESERVED_ANT_PATH_PUBLIC = "/public/**";


    //    @Resource (name = "pathCtrlService")
    @Autowired  //use demo
            BtuPathCtrlService pathCtrlService;

    @PostConstruct
    public void loadResourceDefine() {
        //reloadResourceDefine();
    }

    private void reloadResourceDefine() {
        Map<RequestMatcher, Collection<ConfigAttribute>> newResourceMap = buildResourceMapFromDb();

        if (MapUtils.isEmpty(newResourceMap)) {
            LOG.error("Secured URLs config is empty.");
            LOG.error("Secured URLs config CANNOT be re-loaded from DB.");
        } else {
            resourceMap = newResourceMap;
            LOG.info(String.format("Secured URLs config successfully re-loaded from DB. Applying on %d URLs.",
                    newResourceMap.keySet().size()));
        }
    }

    @SuppressWarnings("Duplicates")
    private Map<RequestMatcher, Collection<ConfigAttribute>> buildResourceMapFromDb() {
        Map<RequestMatcher, Collection<ConfigAttribute>> newResourceMap = new HashMap<>();
        Map<String, RequestMatcher> uriMatcherIndexRefMap = new HashMap<>();

        // get Secured URLs config from db
        List<BtuUserGroupPathCtrlBean> userGroupPathCtrlBeanList = pathCtrlService.getActiveCtrlBeanList();


        // add config form db data
        if (!CollectionUtils.isEmpty(userGroupPathCtrlBeanList)) {
            for (BtuUserGroupPathCtrlBean ctrlBean : userGroupPathCtrlBeanList) {
                if (ctrlBean == null) {
                    continue;
                } else if (StringUtils.isEmpty(ctrlBean.getAntPath())) {
                    continue;
                } else if (StringUtils.isEmpty(ctrlBean.getGroupId())) {
                    continue;
                }

                // find existing / create new request matcher
                String menuUri = ctrlBean.getAntPath();
                RequestMatcher requestMatcher = uriMatcherIndexRefMap.get(menuUri);
                if (requestMatcher == null) {
                    requestMatcher = new AntPathRequestMatcher(menuUri);
                    uriMatcherIndexRefMap.put(menuUri, requestMatcher);
                    newResourceMap.put(requestMatcher, new ArrayList<>());
                }

                // add new required user group config
                Collection<ConfigAttribute> configAttributes = newResourceMap.get(requestMatcher);
                ConfigAttribute configAttribute = new SecurityConfig(ctrlBean.getGroupId());
                configAttributes.add(configAttribute);
            }
        }

        // add or replace config for reserved public access uri
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_ROOT), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_LOGIN), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_RESET_PWD), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_RESET_PWD_OTP), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_JS), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_CSS), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_LIB), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_IMG), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_WEBJAR), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_ERROR), CONFIG_LIST_PERMIT_ALL);
        newResourceMap.put(new AntPathRequestMatcher(RESERVED_ANT_PATH_PUBLIC), CONFIG_LIST_PERMIT_ALL);

        return newResourceMap;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
       /* final HttpServletRequest request = ((FilterInvocation) object).getRequest();

        List<ConfigAttribute> results = new ArrayList<>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : resourceMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                results.addAll(entry.getValue());
            }
        }

        if ( CollectionUtils.isEmpty(results) ){
            // default blocking all, when no specific config is defined
            return CONFIG_LIST_DENY_ALL;
        } else {
            return results;
        }*/
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
       /* Set<ConfigAttribute> allAttributes = new HashSet<>();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : resourceMap
                .entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;*/
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
