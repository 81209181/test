package com.hkt.btu.sd.facade;

import com.hkt.btu.sd.facade.constant.ServiceSearchEnum;

import java.security.GeneralSecurityException;
import java.util.List;

public interface SdServiceTypeUserRoleFacade {

    List<String> getServiceTypeUserRoleByServiceType(String serviceType);

    String editServiceTypeUserRole(String serviceType, List<String> userRoleId, List<String> searchKey) throws GeneralSecurityException;

    List<ServiceSearchEnum> getAllSearchKey();

    List<String> getSearchKeyMapping(String serviceType);
}
