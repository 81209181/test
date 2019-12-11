package com.hkt.btu.sd.core.dao.mapper;

import com.hkt.btu.sd.core.dao.entity.SdUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SdUserMapper {

    Integer getNewUserId();

    SdUserEntity getUserByEmail(@Param("email") String email);

    SdUserEntity getLdapUserByUserId(String userId);

    SdUserEntity getUserByUserId(String userId);


    SdUserEntity getUserByLdapDomain(@Param("ldapDomain") String ldapDomain);

    List<String> getPasswordHistByUserId(@Param("userId") String userId);


    List<SdUserEntity> searchUser(@Param("offset") long offset, @Param("pageSize") int pageSize, String userId, String email, String name);

    Integer countSearchUser(String userId, String email, String name);


    void insertUser(SdUserEntity sdUserEntity);


    void updateUser(@Param("userId") String userId,
                    @Param("name") String name, @Param("mobile") String mobile, @Param("email") String email,@Param("primaryRoleId") String primaryRoleId, @Param("modifyby") String modifyby);

    void updateLdapUser(@Param("userId") String userId,
                        @Param("name") String name,
                        @Param("email") String email);


    void changeUserType(@Param("userId") String userId,
                        @Param("name") String name, @Param("mobile") String mobile, @Param("employeeNumber") String employeeNumber,
                        @Param("ldapDomain") String ldapDomain,
                        @Param("email") String email, @Param("modifyby") String modifyby);

    void updateUserPassword(@Param("userId") String userId, @Param("password") String password);

    void addLoginTriedByUsername(@Param("username") String username);

    void resetLoginTriedByUsername(@Param("username") String username);

    void updateUserStatusByUsername(@Param("username") String username, @Param("status") String status, @Param("modifyby") String modifyby);

    void insertPasswordHist(@Param("userId") String userId, @Param("password") String password);

    List<SdUserEntity> getUserByRoleId(@Param("roleId") String roleId);

    List<SdUserEntity> getAllUser();

    List<SdUserEntity> getTeamHeadUser(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                       String teamHead, String roleId);

    Integer countTeamHeadUser(String teamHead, String roleId);
}
