package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.common.core.dao.entity.BtuConfigParamEntity;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.impl.BtuConfigParamServiceImpl;
import com.hkt.btu.sd.core.dao.entity.SdConfigParamEntity;
import com.hkt.btu.sd.core.dao.entity.SdSymptomEntity;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.dao.mapper.SdSymptomMapper;
import com.hkt.btu.sd.core.service.SdConfigParamService;
import com.hkt.btu.sd.core.service.SdSymptomService;
import com.hkt.btu.sd.core.service.SdUserService;
import com.hkt.btu.sd.core.service.bean.SdConfigParamBean;
import com.hkt.btu.sd.core.service.bean.SdSymptomBean;
import com.hkt.btu.sd.core.service.populator.SdConfigParamBeanPopulator;
import com.hkt.btu.sd.core.service.populator.SdSymptomBeanPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class SdSymptomServiceImpl implements SdSymptomService {
    private static final Logger LOG = LogManager.getLogger(SdSymptomServiceImpl.class);

    @Resource
    private SdSymptomMapper sdSymptomMapper;

    @Resource(name = "symptmBeanPopulator")
    SdSymptomBeanPopulator symptomBeanPopulator;
    @Resource(name = "userService")
    SdUserService userService;

    @Override
    public List<SdSymptomBean> getSymptomGroupList() {
        List<SdSymptomEntity> entityList = sdSymptomMapper.getSymptomGroupList();
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<SdSymptomBean> beanList = new LinkedList<>();
        for (SdSymptomEntity entity : entityList) {
            SdSymptomBean bean = new SdSymptomBean();
            symptomBeanPopulator.populate(entity, bean);
            beanList.add(bean);
        }

        return beanList;
    }

    @Override
    public boolean createSymptom(String symptomCode,String symptomGroupCode,String symptomDescription) {
        return sdSymptomMapper.insertSymptom(symptomCode,symptomGroupCode,symptomDescription,userService.getCurrentUserUserId()) > 0;
    }
}
