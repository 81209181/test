package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserMapper {

    Integer getNewUserId();

    SdUserEntity getUserByEmail(@Param("email") String email);

    SdUserEntity getLdapUserByUserId(String userId);

    SdUserEntity getUserByUserId(String userId, Integer companyId);


    SdUserEntity getUserByLdapDomain(@Param("ldapDomain") String ldapDomain);

    List<String> getPasswordHistByUserId(@Param("userId") String userId);


    List<SdUserEntity> searchUser(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                  Integer companyId, String userId, String email, String name, String userGroupId);

    Integer countSearchUser(Integer companyId, String userId, String email, String name, String userGroupId);


    void insertUser(SdUserEntity sdUserEntity);

    void updateUser(@Param("userId") String userId,
                    @Param("name") String name, @Param("mobile") byte[] mobile, @Param("staffId") byte[] staffId,
                    @Param("modifyby") String modifyby);


    void updateUserPassword(@Param("userId") String userId, @Param("password") String password);

    void addLoginTriedByUsername(@Param("username") String username);

    void resetLoginTriedByUsername(@Param("username") String username);

    void updateUserStatusByUsername(@Param("username") String username, @Param("status") String status, @Param("modifyby") String modifyby);

    void insertPasswordHist(@Param("userId") String userId, @Param("password") String password);


}
