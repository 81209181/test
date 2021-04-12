package com.hkt.btu.sd.facade.data.gmb;

import com.hkt.btu.common.facade.data.DataInterface;

import java.time.LocalDateTime;
import java.util.List;

public class GmbIddInfoData implements DataInterface {

    private String imei;

    private String hwid;

    private String imsi;

    private LocalDateTime gmb_lastUpdateDate;

    private LocalDateTime data_lastUpdateDate;

    private List<GmbErrorData> error;

    private GmbVehicleData vehicle;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public LocalDateTime getGmb_lastUpdateDate() {
        return gmb_lastUpdateDate;
    }

    public void setGmb_lastUpdateDate(LocalDateTime gmb_lastUpdateDate) {
        this.gmb_lastUpdateDate = gmb_lastUpdateDate;
    }

    public LocalDateTime getData_lastUpdateDate() {
        return data_lastUpdateDate;
    }

    public void setData_lastUpdateDate(LocalDateTime data_lastUpdateDate) {
        this.data_lastUpdateDate = data_lastUpdateDate;
    }

    public List<GmbErrorData> getError() {
        return error;
    }

    public void setError(List<GmbErrorData> error) {
        this.error = error;
    }

    public GmbVehicleData getVehicle() {
        return vehicle;
    }

    public void setVehicle(GmbVehicleData vehicle) {
        this.vehicle = vehicle;
    }
}
