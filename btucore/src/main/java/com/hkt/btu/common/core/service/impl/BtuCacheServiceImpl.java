package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.bean.BtuCacheInfoBean;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BtuCacheServiceImpl implements BtuCacheService {

    private static Map<String, Object> CACHE_OBJECT_MAP = new HashMap<>();

    public <T> T get(Class<T> objectType, Object loadingClass, Method loadingMethod)
            throws InvocationTargetException, IllegalAccessException {




        return (T) loadingMethod.invoke(loadingClass);
    }

    public <T> T get(BtuCacheInfoBean cacheInfoBean, Class<T> objectType)
            throws InvocationTargetException, IllegalAccessException {
        return get(objectType, cacheInfoBean.getLoadingClass(), cacheInfoBean.getLoadingMethod());
    }

//    public <T> T get(String cacheId, Class<T> objectType)
//            throws InvocationTargetException, IllegalAccessException {
//        BtuCacheEnum btuCacheEnum = BtuCacheEnum.getEnum(cacheId);
//        if(btuCacheEnum==null){
//            return null;
//        }
//
//        return get(objectType, btuCacheEnum.getLoadingClassBeanName(), btuCacheEnum.getLoadingMethodStr());
//    }



    public void init(){

    }

}
