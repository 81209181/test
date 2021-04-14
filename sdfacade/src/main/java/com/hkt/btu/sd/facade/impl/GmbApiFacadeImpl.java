package com.hkt.btu.sd.facade.impl;

import com.hkt.btu.common.core.annotation.AutoRetry;
import com.hkt.btu.common.core.exception.BtuApiCallException;
import com.hkt.btu.common.core.service.bean.BtuApiProfileBean;
import com.hkt.btu.common.facade.AbstractRestfulApiFacade;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.core.service.SdApiService;
import com.hkt.btu.sd.facade.GmbApiFacade;
import com.hkt.btu.sd.facade.data.gmb.GmbErrorData;
import com.hkt.btu.sd.facade.data.gmb.GmbIddInfoData;
import com.hkt.btu.sd.facade.data.gmb.GmbStatusUpdateData;
import com.hkt.btu.sd.facade.data.gmb.GmbVehicleData;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GmbApiFacadeImpl extends AbstractRestfulApiFacade implements GmbApiFacade {

    private static final Logger LOG = LogManager.getLogger(GmbApiFacadeImpl.class);

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
        queryParamMap.put("plate", plateId);

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

    @AutoRetry(minWaitSecond = 600)
    @Override
    public void notifyTicketStatus(String plateNo, Integer ticketId, String time, String action) {
        LOG.info("Notifying ticket status... (plateNo={}, ticket={}, status={}, time={})", plateNo, ticketId, action, time);
        String apiPath = "/api/servicedesk/notifyTicketStatus";

        // prepare post body
        GmbStatusUpdateData statusUpdateData = new GmbStatusUpdateData();
        statusUpdateData.setPlateNo(plateNo);
        statusUpdateData.setTicketID(ticketId);
        statusUpdateData.setTime(time);
        statusUpdateData.setAction(action);
        Entity<GmbStatusUpdateData> postBodyEntity = Entity.entity(statusUpdateData, MediaType.APPLICATION_JSON_TYPE);

        // check response
        Response response = postEntity(apiPath, postBodyEntity);
        String result = response == null ? null : response.readEntity(String.class);
        if(!StringUtils.equalsIgnoreCase("Success", result)){
            // add to auto retry
            LOG.warn("Response: {}", result);
            throw new BtuApiCallException("API call not success.");
        }
    }
}
