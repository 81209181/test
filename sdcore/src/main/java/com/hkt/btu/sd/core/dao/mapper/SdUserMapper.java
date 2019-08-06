package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserMapper {

    Integer getNewUserId();

    SdUserEntity getUserByEmail(@Param("email") String email);
    SdUserEntity getUserByUserId(Integer userId, Integer companyId);
    List<String> getPasswordHistByUserId(@Param("userId") Integer userId);


    List<SdUserEntity> searchUser(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                   Integer companyId, Integer userId, String email, String name, String userGroupId);
    Integer countSearchUser(Integer companyId, Integer userId, String email, String name, String userGroupId);


    void insertUser(SdUserEntity sdUserEntity);
    void updateUser(@Param("userId") Integer userId,
                    @Param("name") String name, @Param("mobile") byte[] mobile, @Param("staffId") byte[] staffId,
                    @Param("modifyby") Integer modifyby);


    void updateUserPassword(@Param("userId") Integer userId, @Param("password") String password);

    void addLoginTriedByUsername(@Param("username") String username);
    void resetLoginTriedByUsername(@Param("username") String username);
    void updateUserStatusByUsername(@Param("username") String username, @Param("status") String status, @Param("modifyby") Integer modifyby);

    void insertPasswordHist(@Param("userId") Integer userId, @Param("password") String password);

}
