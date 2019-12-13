package com.hkt.btu.common.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkt.btu.common.core.service.BtuCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BtuCacheServiceImpl implements BtuCacheService {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String getCache(String cacheName) {
        Object bean = applicationContext.getBean(cacheName);
        try {
            Method getServiceTypeList = bean.getClass().getMethod("getServiceTypeList");
            Object returnObj = getServiceTypeList.invoke(bean);
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(returnObj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
