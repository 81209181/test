package com.hkt.btu.sd.facade.data.oss;

import com.hkt.btu.common.facade.data.DataInterface;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class OssSmartMeterEventData implements DataInterface {
    private Integer eventId;
    private String eventCode;
    private String eventDesc;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime eventTime;


}
