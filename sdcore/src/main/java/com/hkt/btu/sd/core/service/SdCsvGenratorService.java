package com.hkt.btu.sd.core.service;

import com.hkt.btu.common.core.service.bean.BtuReportMetaDataBean;

import java.io.File;

public interface SdCsvGenratorService {

    File getCsvFile(BtuReportMetaDataBean metaData);
}
