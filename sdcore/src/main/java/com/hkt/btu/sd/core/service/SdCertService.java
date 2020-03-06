package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.bean.BtuConfigParamBean;
import com.hkt.btu.sd.core.service.bean.SdCheckCertBean;

import java.util.List;

public interface SdCertService {

    List<SdCheckCertBean> checkCert(List<BtuConfigParamBean> configParamBeanList);

    String formEmailBody(List<SdCheckCertBean> checkCertBeanList);
}
