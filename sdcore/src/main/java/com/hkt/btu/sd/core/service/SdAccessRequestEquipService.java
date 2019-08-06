package com.hkt.btu.sd.core.service;


import com.hkt.btu.sd.core.exception.AuthorityNotFoundException;
import com.hkt.btu.sd.core.service.bean.SdAccessRequestEquipBean;
import com.hkt.btu.sd.core.service.bean.SdUserBean;

import java.util.List;

public interface SdAccessRequestEquipService {

    List<SdAccessRequestEquipBean> getEquipListByAccessRequestId(Integer accessRequestId) throws AuthorityNotFoundException;

    void createEquipForAccessRequest(List<SdAccessRequestEquipBean> visitorBeanList, Integer newAccessRequestId, SdUserBean requesterUserBean);

}
