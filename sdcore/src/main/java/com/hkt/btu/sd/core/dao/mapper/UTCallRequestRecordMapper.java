package com.hkt.btu.sd.core.dao.mapper;

//import com.hkt.btu.sd.core.dao.entity.SdUserRoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.hkt.btu.sd.core.dao.entity.UTCallRequestEntity;

import java.util.List;
import java.util.Map;

@Repository
public interface UTCallRequestRecordMapper {
    List<UTCallRequestEntity> getUTCallRequestRecordList();
    void insertNewUTCallRequestRecord(@Param("bsnNum") String bsnNum, @Param("code") String code,
                             @Param("msg") String msg, @Param("serviceCode") String serviceCode,
                             @Param("seq") String seq, @Param("seqType") String seqType, @Param("createBy") String createBy);
}