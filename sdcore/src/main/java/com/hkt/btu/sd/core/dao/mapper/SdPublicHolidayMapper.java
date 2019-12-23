package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdPublicHolidayEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdPublicHolidayMapper {

    List<SdPublicHolidayEntity> getPublicHolidayList(@Param("offset") long offset, @Param("pageSize") int pageSize, @Param("year") String year);

    Integer countPublicHoliday(@Param("year") String year);

    List<SdPublicHolidayEntity> getAllPublicHolidayList();

    void insertPublicHoliday(@Param("entityList") List<SdPublicHolidayEntity> entityList);
}
