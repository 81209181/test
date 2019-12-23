package com.hkt.btu.sd.core.service;

import com.hkt.btu.sd.core.service.bean.SdPublicHolidayBean;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SdPublicHolidayService {
    Page<SdPublicHolidayBean> getPublicHolidayList(Pageable pageable, String year);

    void deletePublicHoliday(String publicHoliday, String description);

    void createPublicHoliday(String publicHoliday, String description);

    void checkPublicHoliday() throws JobExecutionException;
    List<SdPublicHolidayBean> getAllPublicHolidayList();

    void insertPublicHoliday(List<SdPublicHolidayBean> beanList);
}
