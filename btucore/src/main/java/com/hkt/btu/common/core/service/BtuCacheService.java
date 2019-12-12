package com.hkt.btu.common.core.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface BtuCacheService {

    <T> T get(Class<T> objectType, Object loadingClass, Method loadingMethod) throws InvocationTargetException, IllegalAccessException;


}
