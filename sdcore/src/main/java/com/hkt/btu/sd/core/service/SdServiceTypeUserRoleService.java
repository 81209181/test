package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.sd.core.service.bean.SdServiceTypeUserRoleBean;

import java.util.List;

public interface SdServiceTypeUserRoleService {

    List<SdServiceTypeUserRoleBean> getServiceTypeUserRoleByServiceType(String serviceType);

    void editServiceTypeUserRole(String serviceType, List<String> userRoleId);

    List<SdServiceTypeUserRoleBean> loadServiceTypeUserRole();

    List<BtuConfigParamBean> loadSearchKeyTypeMapping();
}
