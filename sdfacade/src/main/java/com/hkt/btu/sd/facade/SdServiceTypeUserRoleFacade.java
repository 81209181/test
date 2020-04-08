package com.hkt.btu.sd.facade;

import java.util.List;

public interface SdServiceTypeUserRoleFacade {

    List<String> getServiceTypeUserRoleByServiceType(String serviceType);

    String editServiceTypeUserRole(String serviceType, List<String> userRoleId);
}
