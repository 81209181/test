package com.hkt.btu.common.core.dao.mapper;

import com.hkt.btu.common.core.dao.entity.BtuUserEntity;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BtuUserMapper {

    Integer getNewUserId();

    BtuUserEntity getUserByEmail(@Param("email") String email);

    BtuUserEntity getUserByUserId(Integer userId, Integer companyId);

    List<String> getPasswordHistByUserId(@Param("userId") Integer userId);


    List<BtuUserEntity> searchUser(@Param("offset") long offset, @Param("pageSize") int pageSize,
                                   Integer companyId, Integer userId, String email, String name, String userGroupId);

    Integer countSearchUser(Integer companyId, Integer userId, String email, String name, String userGroupId);


    void insertUser(BtuUserEntity userEntity);

    void updateUser(@Param("userId") Integer userId,
                    @Param("name") String name, @Param("mobile") byte[] mobile, @Param("staffId") byte[] staffId,
                    @Param("modifyby") Integer modifyby);


    void updateUserPassword(@Param("userId") Integer userId, @Param("password") String password);

    void addLoginTriedByUsername(@Param("username") String username);

    void resetLoginTriedByUsername(@Param("username") String username);

    void updateUserStatusByUsername(@Param("username") String username, @Param("status") String status, @Param("modifyby") Integer modifyby);

    void insertPasswordHist(@Param("userId") Integer userId, @Param("password") String password);


}
