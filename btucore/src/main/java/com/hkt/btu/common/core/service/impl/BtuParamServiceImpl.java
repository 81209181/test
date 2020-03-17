package com.hkt.btu.common.core.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.core.service.BtuParamService;
import com.hkt.btu.common.core.service.bean.BtuParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import com.hkt.btu.common.core.util.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
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
//            result = JsonUtils.obj2String(paramList);
            result = new Gson().toJson(paramList);
        }

        return result;
    }

    @Override
    public Object[] deserialize(String paramListJson) {
        // todo: reference below, use gson instead of JsonUtils
        // com/hkt/btu/sd/facade/impl/WfmApiFacadeImpl.java.getJobRemarkByTicketId
        // Type type = new TypeToken<List<BtuParamBean>>() {}.getType();

        BtuParamBean[] array = new Gson().fromJson(paramListJson,BtuParamBean[].class);
        List<BtuParamBean> paramList = Arrays.asList(array);
        Object[] objArray = null;

        if(CollectionUtils.isNotEmpty(paramList)){
            objArray = paramList.toArray();
        }

        return objArray;
    }
}
