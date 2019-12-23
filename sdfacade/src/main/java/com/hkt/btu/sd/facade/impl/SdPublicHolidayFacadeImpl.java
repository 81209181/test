package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.SdPublicHolidayService;
import com.hkt.btu.sd.core.service.bean.SdPublicHolidayBean;
import com.hkt.btu.sd.facade.SdPublicHolidayFacade;
import com.hkt.btu.sd.facade.data.SdPublicHolidayData;
import com.hkt.btu.sd.facade.populator.SdPublicHolidayDataPopulator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;


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
    public boolean createPublicHoliday(String publicHoliday, String description) {
        if (StringUtils.isEmpty(publicHoliday) || StringUtils.isEmpty(description)) {
            return false;
        }
        try {
            sdPublicHolidayService.createPublicHoliday(publicHoliday, description);
            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return false;
    }
}
