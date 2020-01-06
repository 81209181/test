package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.entity.SdPublicHolidayEntity;
import com.hkt.btu.sd.core.dao.mapper.SdPublicHolidayMapper;
import com.hkt.btu.sd.core.service.SdPublicHolidayService;
import com.hkt.btu.sd.core.service.bean.SdPublicHolidayBean;
import com.hkt.btu.sd.core.service.populator.SdPublicHolidayBeanPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SdPublicHolidayServiceImpl implements SdPublicHolidayService {
    private static final Logger LOG = LogManager.getLogger(SdPublicHolidayServiceImpl.class);

    @Resource
    SdPublicHolidayMapper sdPublicHolidayMapper;

    @Resource(name = "publicHolidayBeanPopulator")
    SdPublicHolidayBeanPopulator publicHolidayBeanPopulator;

    @Override
    public Page<SdPublicHolidayBean> getPublicHolidayList(Pageable pageable, String year) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();

        List<SdPublicHolidayEntity> entityList = sdPublicHolidayMapper.getPublicHolidayList(offset, pageSize, year);
        Integer totalCount = sdPublicHolidayMapper.countPublicHoliday(year);

        List<SdPublicHolidayBean> beanList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            for (SdPublicHolidayEntity entity : entityList) {
                SdPublicHolidayBean bean = new SdPublicHolidayBean();
                publicHolidayBeanPopulator.populate(entity, bean);
                beanList.add(bean);
            }
        }

        return new PageImpl<>(beanList, pageable, totalCount);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePublicHoliday(String publicHoliday, String description) {
        sdPublicHolidayMapper.deletePublicHoliday(publicHoliday, description);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPublicHoliday(String publicHoliday, String description) {
        sdPublicHolidayMapper.createPublicHoliday(publicHoliday, description);
    }

    @Override
    public void checkPublicHoliday() throws JobExecutionException {
        List<SdPublicHolidayBean> beanList = getAllPublicHolidayList();

        if (!CollectionUtils.isEmpty(beanList)) {
            SdPublicHolidayBean bean = beanList.get(beanList.size() - 1);
            LocalDate cutoffDate = LocalDate.now().withDayOfMonth(1).minusMonths(-3);

            LOG.info("Check public holiday after: {}", cutoffDate);
            LOG.info("Furthest public holiday date: {}", bean.getPublicHoliday());

            if (bean.getPublicHoliday().isAfter(cutoffDate)) {
                return;
            } else {
                throw new JobExecutionException("Not found public holiday after next three months.");
            }
        }

        throw new JobExecutionException("Not found public holiday in DB.");
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

        if (CollectionUtils.isEmpty(entityList)) {
            return;
        } else {
            sdPublicHolidayMapper.insertPublicHoliday(entityList);
        }
    }
}
