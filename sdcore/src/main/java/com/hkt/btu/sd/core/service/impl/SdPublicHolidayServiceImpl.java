package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdPublicHolidayEntity;
import com.hkt.btu.sd.core.dao.mapper.SdPublicHolidayMapper;
import com.hkt.btu.sd.core.service.SdPublicHolidayService;
import com.hkt.btu.sd.core.service.bean.SdPublicHolidayBean;
import com.hkt.btu.sd.core.service.populator.SdPublicHolidayBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SdPublicHolidayServiceImpl implements SdPublicHolidayService {

    @Resource
    SdPublicHolidayMapper sdPublicHolidayMapper;

    @Resource(name = "publicHolidayBeanPopulator")
    SdPublicHolidayBeanPopulator publicHolidayBeanPopulator;

    @Override
    public Page<SdPublicHolidayBean> getPublicHolidayList(Pageable pageable, String year) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<SdPublicHolidayEntity> sdUserEntityList = sdPublicHolidayMapper.getPublicHolidayList(offset, pageSize, year);
        Integer totalCount = sdPublicHolidayMapper.countPublicHoliday(year);

        List<SdPublicHolidayBean> sdUserBeanList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(sdUserEntityList)) {
            for (SdPublicHolidayEntity sdUserEntity : sdUserEntityList) {
                SdPublicHolidayBean sdUserBean = new SdPublicHolidayBean();
                publicHolidayBeanPopulator.populate(sdUserEntity, sdUserBean);
                sdUserBeanList.add(sdUserBean);
            }
        }

        return new PageImpl<>(sdUserBeanList, pageable, totalCount);
    }

    @Override
    public List<SdPublicHolidayBean> getAllPublicHolidayList() {
        List<SdPublicHolidayEntity> allPublicHolidayList = sdPublicHolidayMapper.getAllPublicHolidayList();

        if (CollectionUtils.isEmpty(allPublicHolidayList)) {
            return null;
        }

        return allPublicHolidayList.stream().map(entity ->{
            SdPublicHolidayBean bean = new SdPublicHolidayBean();
            publicHolidayBeanPopulator.populate(entity, bean);
            return bean;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertPublicHoliday(List<SdPublicHolidayBean> beanList) {
        if (CollectionUtils.isEmpty(beanList)) {
            return;
        }

        List<SdPublicHolidayEntity> entityList = beanList.stream().map(bean -> {
            SdPublicHolidayEntity entity = new SdPublicHolidayEntity();
            publicHolidayBeanPopulator.populate(bean, entity);
            return entity;
        }).collect(Collectors.toList());

        sdPublicHolidayMapper.insertPublicHoliday(entityList);
    }
}
