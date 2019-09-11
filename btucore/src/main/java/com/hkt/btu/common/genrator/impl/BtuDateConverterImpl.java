package com.hkt.btu.common.genrator.impl;

import com.hkt.btu.common.genrator.BtuDateConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class BtuDateConverterImpl implements BtuDateConverter {

    private static final Logger LOG = LogManager.getLogger(BtuDateConverterImpl.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public String convert(Object object, String dateFormat) {
        String result = "";
        if (object != null) {
            dateFormat = Optional.ofNullable(dateFormat).orElse(DATE_FORMAT);
            result = convertDateToString((Timestamp) object, dateFormat);
        }
        return result;
    }

    private String convertDateToString(Timestamp timestamp, String dateFormat) {
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            tsStr = sdf.format(timestamp);
        } catch (Exception e) {
            LOG.warn("Date convert has error.");
        }
        return tsStr;
    }
}
