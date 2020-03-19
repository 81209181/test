package com.hkt.btu.common.core.service.impl;

import com.google.gson.reflect.TypeToken;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuParamService;
import com.hkt.btu.common.core.service.bean.BtuParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import com.hkt.btu.common.core.util.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class BtuParamServiceImpl implements BtuParamService {


    @Override
    public String serialize(Object[] objArray) {
        List<BtuParamBean> paramList = new LinkedList<>();
        String result = null;

        if(objArray != null && objArray.length > 0){
            for (Object o : objArray) {
                BtuParamBean bean = new BtuParamBean();
                if (o instanceof String) {
                    bean.setParamType(BtuConfigParamTypeEnum.STRING);
                    bean.setValue(o.toString());
                    paramList.add(bean);
                } else if (o instanceof Integer) {
                    bean.setParamType(BtuConfigParamTypeEnum.INTEGER);
                    bean.setValue(o.toString());
                    paramList.add(bean);
                } else if (o instanceof Double) {
                    bean.setParamType(BtuConfigParamTypeEnum.DOUBLE);
                    bean.setValue(o.toString());
                    paramList.add(bean);
                } else if (o instanceof Boolean) {
                    bean.setParamType(BtuConfigParamTypeEnum.BOOLEAN);
                    bean.setValue(o.toString());
                    paramList.add(bean);
                } else if (o instanceof LocalDateTime) {
                    bean.setParamType(BtuConfigParamTypeEnum.LOCAL_DATE_TIME);
                    bean.setValue(o.toString());
                    paramList.add(bean);
                } else {
                    throw new InvalidInputException("Unsupported type for re-try.");
                }
            }
        }

        if(CollectionUtils.isNotEmpty(paramList)){
            result = JsonUtils.obj2String(paramList);
        }

        return result;
    }

    @Override
    public Object[] deserialize(String paramListJson) {
        Type type = new TypeToken<List<BtuParamBean>>() {}.getType();
        List<BtuParamBean> paramList = JsonUtils.string2Obj(paramListJson, type);

        if(CollectionUtils.isNotEmpty(paramList)){
            Object[] objArray = new Object[paramList.size()];
            paramList.forEach(param -> {
                int i = paramList.indexOf(param);
                if (param.getParamType().equals(BtuConfigParamTypeEnum.STRING)) {
                    objArray[i] = param.getValue();
                } else if (param.getParamType().equals(BtuConfigParamTypeEnum.INTEGER)) {
                    objArray[i] = Integer.parseInt(param.getValue());
                } else if (param.getParamType().equals(BtuConfigParamTypeEnum.DOUBLE)) {
                    objArray[i] = Double.parseDouble(param.getValue());
                } else if (param.getParamType().equals(BtuConfigParamTypeEnum.BOOLEAN)) {
                    objArray[i] = BooleanUtils.toBoolean(param.getValue());
                } else if (param.getParamType().equals(BtuConfigParamTypeEnum.LOCAL_DATE_TIME)) {
                    objArray[i] = LocalDateTime.parse(param.getValue());
                }
            });
            return objArray;
        }

        return new Object[]{};
    }
}
