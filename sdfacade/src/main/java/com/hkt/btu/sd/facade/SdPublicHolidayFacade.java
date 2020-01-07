package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.SdPublicHolidayData;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SdPublicHolidayFacade {
    PageData<SdPublicHolidayData> getPublicHolidayList(Pageable pageable, String year);

    boolean deletePublicHoliday(String publicHoliday, String description);

    void createPublicHoliday(String publicHoliday, String description);

    List<SdPublicHolidayData> getAllPublicHolidayList();

    boolean addPublicHoliday(List<SdPublicHolidayData> data);
}
