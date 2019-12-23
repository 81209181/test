package com.hkt.btu.sd.facade;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.facade.data.SdPublicHolidayData;
import org.springframework.data.domain.Pageable;

public interface SdPublicHolidayFacade {
    PageData<SdPublicHolidayData> getPublicHolidayList(Pageable pageable, String year);

    boolean deletePublicHoliday(String publicHoliday, String description);

    boolean createPublicHoliday(String publicHoliday, String description);
}
