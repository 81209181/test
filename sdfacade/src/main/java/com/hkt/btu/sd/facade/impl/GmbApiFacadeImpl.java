package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;
import com.hkt.btu.common.facade.AbstractRestfulApiFacade;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.facade.GmbApiFacade;
import com.hkt.btu.sd.facade.data.gmb.GmbErrorData;
import com.hkt.btu.sd.facade.data.gmb.GmbIddInfoData;
import com.hkt.btu.sd.facade.data.gmb.GmbVehicleData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GmbApiFacadeImpl extends AbstractRestfulApiFacade implements GmbApiFacade {

    @Resource(name = "apiService")
    SdApiService apiService;

    @Override
    protected BtuApiProfileBean getTargetApiProfile() {
        return apiService.getGmbApiProfileBean();
    }

    @Override
    public GmbIddInfoData getIddInfo(String plateId) {
        if (StringUtils.isEmpty(plateId)) {
            return null;
        }

        // add query param
        Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("plateNo", plateId);

        GmbIddInfoData iddInfoData = getData("/api/servicedesk/lddInfo", GmbIddInfoData.class, queryParamMap);

        return iddInfoData == null ? null : iddInfoData;
    }

    @Override
    public PageData<GmbErrorData> getErrorList(Pageable pageable, String plateId) {
        GmbIddInfoData iddInfoData = getIddInfo(plateId);

        if (iddInfoData == null) {
            return new PageData<>();
        }

        List<GmbErrorData> gmbErrorDataList = iddInfoData.getError();
        return new PageData<>(gmbErrorDataList, pageable, gmbErrorDataList.size());
    }

    @Override
    public GmbVehicleData getVehicleInfo(String plateId) {
        GmbIddInfoData iddInfoData = getIddInfo(plateId);

        if (iddInfoData == null) {
            return null;
        }

        return iddInfoData.getVehicle();
    }
}
