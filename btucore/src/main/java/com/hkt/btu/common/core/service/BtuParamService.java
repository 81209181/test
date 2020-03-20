package com.hkt.btu.common.core.service;

public interface BtuParamService {

    String serialize(Object[] paramList);
    Object[] deserialize(String paramListJson);
    Class[] getParameterTypes(Object[] objArray);
}
