package com.hkt.btu.sd.core.dao.mapper;

//import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
//import com.hkt.btu.sd.core.dao.entity.UTCallRequestEntity;

import java.util.List;
import java.util.Map;

@Repository
public interface UTCallResultRecordMapper {
    void insertNewUTCallResultRecord(@Param("utCallId") String utCallId, @Param("code") String code,
                                      @Param("msg") String msg, @Param("utSummaryString") String utSummaryString,
                                      @Param("createBy") String createBy);

    void updateUTCallResultRecord(@Param("utCallId") String utCallId, @Param("code") String code,
                                  @Param("msg") String msg, @Param("utSummaryString") String utSummaryString,
                                  @Param("modifyBy") String modifyBy);

    Integer utCallResultRecordExist(@Param("utCallId") String utCallId);
}