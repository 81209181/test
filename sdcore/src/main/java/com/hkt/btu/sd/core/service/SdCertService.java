package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.sd.core.service.bean.SdCheckCertBean;

import java.util.List;

public interface SdCertService {

    List<SdCheckCertBean> checkCert(List<String> hostList, BtuSiteConfigBean siteConfigBean);

    String formEmailBody(List<SdCheckCertBean> checkCertBeanList);
}
