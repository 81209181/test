package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.exception.InvalidInputException;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.SdPublicHolidayService;
import com.hkt.btu.sd.core.service.bean.SdPublicHolidayBean;
import com.hkt.btu.sd.facade.SdPublicHolidayFacade;
import com.hkt.btu.sd.facade.data.SdPublicHolidayData;
import com.hkt.btu.sd.facade.populator.SdPublicHolidayDataPopulator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class SdPublicHolidayFacadeImpl implements SdPublicHolidayFacade {

    private static final Logger LOG = LogManager.getLogger(SdPublicHolidayFacadeImpl.class);

    @Resource(name = "sdPublicHolidayService")
    SdPublicHolidayService sdPublicHolidayService;

    @Resource(name = "publicHolidayDataPopulator")
    SdPublicHolidayDataPopulator publicHolidayDataPopulator;

    @Override
    public PageData<SdPublicHolidayData> getPublicHolidayList(Pageable pageable, String year) {
        Page<SdPublicHolidayBean> pageBean;
        try {
            // default search result of current year
            if (StringUtils.isEmpty(year)) {
                year = String.valueOf(LocalDateTime.now().getYear());
            }
            pageBean = sdPublicHolidayService.getPublicHolidayList(pageable, year);
        } catch (AuthorityNotFoundException e) {
            return new PageData<>(e.getMessage());
        }

        // populate content
        List<SdPublicHolidayBean> beanList = pageBean.getContent();
        List<SdPublicHolidayData> dataList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(beanList)) {
            for (SdPublicHolidayBean bean : beanList) {
                SdPublicHolidayData data = new SdPublicHolidayData();
                publicHolidayDataPopulator.populate(bean, data);
                dataList.add(data);
            }
        }

        return new PageData<>(dataList, pageBean.getPageable(), pageBean.getTotalElements());
    }

    @Override
    public boolean deletePublicHoliday(String publicHoliday, String description) {
        if (StringUtils.isEmpty(publicHoliday) || StringUtils.isEmpty(description)) {
            return false;
        }
        try {
            sdPublicHolidayService.deletePublicHoliday(publicHoliday, description);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return false;
    }

    @Override
    public void createPublicHoliday(String publicHoliday, String description) {
        if (StringUtils.isEmpty(publicHoliday)) {
            throw new InvalidInputException("Public holiday is empty.");
        } else if (StringUtils.isEmpty(description)) {
            throw new InvalidInputException("Description is empty.");
        }

        try {
            sdPublicHolidayService.createPublicHoliday(publicHoliday, description);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Duplicate data already exists.");
        }
    }

    @Override
    public List<SdPublicHolidayData> getAllPublicHolidayList() {
        List<SdPublicHolidayBean> allPublicHolidayList = sdPublicHolidayService.getAllPublicHolidayList();

        if (CollectionUtils.isEmpty(allPublicHolidayList)) {
            return null;
        }

        return allPublicHolidayList.stream().map(bean -> {
            SdPublicHolidayData data = new SdPublicHolidayData();
            publicHolidayDataPopulator.populate(bean, data);
            return data;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean addPublicHoliday(List<SdPublicHolidayData> data) {
        if (CollectionUtils.isEmpty(data)) {
            return false;
        }

        // check duplicate element
        int duplicateSize = (int) data.stream().distinct().count();
        if (data.size() != duplicateSize) {
            return false;
        }

        // filter null element
        List<SdPublicHolidayData> filterList = data.stream()
                .filter(pbData -> pbData.getPublicHoliday() != null && pbData.getDescription() != null)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterList)) {
            return false;
        }

        try {
            List<SdPublicHolidayData> allPublicHolidayList = getAllPublicHolidayList();

            Collection<SdPublicHolidayData> intersection = CollectionUtils.intersection(filterList, allPublicHolidayList);
            filterList.removeAll(intersection);
            if (CollectionUtils.isEmpty(data)) {
                return true;
            }

            List<SdPublicHolidayBean> beanList = filterList.stream().map(pbData -> {
                SdPublicHolidayBean bean = new SdPublicHolidayBean();
                publicHolidayDataPopulator.populate(pbData, bean);
                return bean;
            }).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(beanList)) {
                return false;
            }
            sdPublicHolidayService.insertPublicHoliday(beanList);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return false;
        }
    }
}
