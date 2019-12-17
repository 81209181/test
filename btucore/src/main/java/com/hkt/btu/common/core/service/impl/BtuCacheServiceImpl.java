package com.hkt.btu.common.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

public class BtuCacheServiceImpl implements BtuCacheService {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String getCache(String cacheId) throws Exception {
        BtuCacheEnum btuCacheEnum = BtuCacheEnum.valueOf(cacheId);
        Object bean = applicationContext.getBean(btuCacheEnum.getServiceName());
        Method method = bean.getClass().getMethod(btuCacheEnum.getCacheBeanMethodName());
        Object returnObj = method.invoke(bean);
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(returnObj);
    }

    @Override
    public void reloadCache(String cacheId) throws Exception {
        BtuCacheEnum btuCacheEnum = BtuCacheEnum.valueOf(cacheId);
        Object bean = applicationContext.getBean(btuCacheEnum.getServiceName());
        Method method = bean.getClass().getMethod(btuCacheEnum.getReloadMethodName());
        method.invoke(bean);
    }


}
