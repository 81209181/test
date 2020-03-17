package com.hkt.btu.common.core.service.impl;

import com.google.gson.Gson;
import com.hkt.btu.common.core.service.BtuParamService;
import com.hkt.btu.common.core.service.bean.BtuParamBean;
import com.hkt.btu.common.core.service.constant.BtuConfigParamTypeEnum;
import com.hkt.btu.common.core.util.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;

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
            for (int i = 0; i < objArray.length; i++) {
                BtuParamBean bean = new BtuParamBean();
                if(objArray[i] instanceof String){
                    bean.setParamType(BtuConfigParamTypeEnum.STRING);
                    bean.setValue(objArray[i].toString());
                    paramList.add(bean);
                }else if(objArray[i] instanceof Integer){
                    bean.setParamType(BtuConfigParamTypeEnum.INTEGER);
                    bean.setValue(objArray[i].toString());
                    paramList.add(bean);
                }else if(objArray[i] instanceof Double){
                    bean.setParamType(BtuConfigParamTypeEnum.DOUBLE);
                    bean.setValue(objArray[i].toString());
                    paramList.add(bean);
                }else if(objArray[i] instanceof Boolean){
                    bean.setParamType(BtuConfigParamTypeEnum.BOOLEAN);
                    bean.setValue(objArray[i].toString());
                    paramList.add(bean);
                }else if(objArray[i] instanceof LocalDateTime){
                    bean.setParamType(BtuConfigParamTypeEnum.LOCAL_DATE_TIME);
                    bean.setValue(objArray[i].toString());
                    paramList.add(bean);
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
        BtuParamBean[] array = new Gson().fromJson(paramListJson,BtuParamBean[].class);
        List<BtuParamBean> paramList = Arrays.asList(array);
        Object[] objArray = null;

        if(CollectionUtils.isNotEmpty(paramList)){
            objArray = paramList.toArray();
        }

        return objArray;
    }
}
