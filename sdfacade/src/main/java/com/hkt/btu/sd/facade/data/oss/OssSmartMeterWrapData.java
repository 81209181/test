package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.common.facade.data.DataInterface;

public class OssSmartMeterWrapData implements DataInterface {
    private OssSmartMeterData meter;

    public OssSmartMeterData getMeter() {
        return meter;
    }

    public void setMeter(OssSmartMeterData meter) {
        this.meter = meter;
    }
}
