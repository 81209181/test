package com.hkt.btu.noc.core.service;


import com.hkt.btu.noc.core.exception.AuthorityNotFoundException;
import com.hkt.btu.noc.core.service.bean.NocAccessRequestEquipBean;
import com.hkt.btu.noc.core.service.bean.NocUserBean;

import java.util.List;

public interface NocAccessRequestEquipService {

    List<NocAccessRequestEquipBean> getEquipListByAccessRequestId(Integer accessRequestId) throws AuthorityNotFoundException;

    void createEquipForAccessRequest(List<NocAccessRequestEquipBean> visitorBeanList, Integer newAccessRequestId, NocUserBean requesterUserBean);

}
