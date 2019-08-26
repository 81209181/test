package com.hkt.btu.sd.core.service.impl;

import com.hkt.btu.sd.core.dao.mapper.SdUserRoleMapper;
import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.core.service.populator.SdUserRoleBeanPopulator;

import javax.annotation.Resource;

public class SdUserRoleServiceImpl implements SdUserRoleService {

    @Resource
    SdUserRoleMapper sdUserRoleMapper;

    @Resource(name = "userRolePopulator")
    SdUserRoleBeanPopulator sdUserRoleBeanPopulator;

}
