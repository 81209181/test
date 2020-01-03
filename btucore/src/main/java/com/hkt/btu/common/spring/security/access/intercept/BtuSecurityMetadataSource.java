package com.hkt.btu.common.spring.security.access.intercept;


import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.BtuPathCtrlService;
import com.hkt.btu.common.core.service.bean.BtuUserRolePathCtrlBean;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


public class BtuSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

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
    public static final String RESERVED_ANT_PATH_LIB = "/lib/**";
    public static final String RESERVED_ANT_PATH_IMG = "/img/**";
    public static final String RESERVED_ANT_PATH_WEBJAR = "/webjars/**";
    public static final String RESERVED_ANT_PATH_ERROR = "/error/**";
    public static final String RESERVED_ANT_PATH_PUBLIC = "/public/**";


    @Resource(name = "pathCtrlService")
    BtuPathCtrlService pathCtrlService;
    @Resource(name = "cacheService")
    BtuCacheService btuCacheService;


    public void reloadResourceDefine() {
        btuCacheService.reloadCachedObject(BtuCacheEnum.RESOURCE_MAP.getCacheName());
    }

    // cached Secured URLs config
    private Map<RequestMatcher, Collection<ConfigAttribute>> getResourceMap(){
        return (Map<RequestMatcher, Collection<ConfigAttribute>>) btuCacheService.getCachedObjectByCacheName(BtuCacheEnum.RESOURCE_MAP.getCacheName());
    }

    @SuppressWarnings("Duplicates")
    public Map<RequestMatcher, Collection<ConfigAttribute>> buildResourceMapFromDb() {
        Map<RequestMatcher, Collection<ConfigAttribute>> newResourceMap = new HashMap<>();
        Map<String, RequestMatcher> uriMatcherIndexRefMap = new HashMap<>();

        // get Secured URLs config from db
        List<BtuUserRolePathCtrlBean> userGroupPathCtrlBeanList =pathCtrlService.getActiveCtrlBeanList();


        // add config form db data
        if (!CollectionUtils.isEmpty(userGroupPathCtrlBeanList)) {
            for (BtuUserRolePathCtrlBean ctrlBean : userGroupPathCtrlBeanList) {
                if (ctrlBean == null) {
                    continue;
                } else if (StringUtils.isEmpty(ctrlBean.getPath())) {
                    continue;
                } else if (StringUtils.isEmpty(ctrlBean.getRoleId())) {
                    continue;
                }

                // find existing / create new request matcher
                String menuUri = ctrlBean.getPath();
                RequestMatcher requestMatcher = uriMatcherIndexRefMap.get(menuUri);
                if (requestMatcher == null) {
                    requestMatcher = new AntPathRequestMatcher(menuUri);
                    uriMatcherIndexRefMap.put(menuUri, requestMatcher);
                    newResourceMap.put(requestMatcher, new ArrayList<>());
                }

                // add new required user group config
                Collection<ConfigAttribute> configAttributes = newResourceMap.get(requestMatcher);
                ConfigAttribute configAttribute = new SecurityConfig(ctrlBean.getRoleId());
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
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();

        List<ConfigAttribute> results = new ArrayList<>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : getResourceMap().entrySet()) {
            if (entry.getKey().matches(request)) {
                results.addAll(entry.getValue());
            }
        }

        if ( CollectionUtils.isEmpty(results) ){
            // default blocking all, when no specific config is defined
            return CONFIG_LIST_DENY_ALL;
        } else {
            return results;
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : getResourceMap().entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
