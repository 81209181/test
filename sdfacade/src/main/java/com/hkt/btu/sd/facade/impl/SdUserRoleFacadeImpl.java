package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.sd.core.service.SdUserRoleService;
import com.hkt.btu.sd.facade.SdUserRoleFacade;

import javax.annotation.Resource;

public class SdUserRoleFacadeImpl implements SdUserRoleFacade {

    @Resource(name = "roleService")
    SdUserRoleService sdUserRoleService;


}
