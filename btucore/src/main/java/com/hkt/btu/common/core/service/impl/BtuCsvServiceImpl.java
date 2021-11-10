package com.hkt.btu.common.core.service.impl;

import au.com.bytecode.opencsv.CSVWriter;
import com.hkt.btu.common.core.service.BtuCsvService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BtuCsvServiceImpl implements BtuCsvService {
    private static final Logger LOG = LogManager.getLogger(BtuCsvServiceImpl.class);

    private final static char DEFAULT_SEPARATOR = ',';
    private final static DateTimeFormatter DEFAULT_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public File generateCSV(String filePath, Character separator, List<Map<String, Object>> content) {
        separator = Optional.ofNullable(separator).orElse(DEFAULT_SEPARATOR);
        try (CSVWriter csvWriter = prepareCSVWriter(filePath, separator)) {
            LOG.info("Generating CSV......");
            // Write Header
            String[] header = getHeader(content);
            csvWriter.writeNext(header);

            // Writer Content
            List<String[]> csvContent = getCsvContent(content);
            csvWriter.writeAll(csvContent);

            File csvFile = new File(filePath);
            LOG.info("Generated CSV successfully! (path:{})", filePath);
            return csvFile;
        } catch (IOException e) {
            LOG.warn("Generate CSV has error:" + e.getMessage());
        }
        return null;
    }

    private CSVWriter prepareCSVWriter(String filePath, char separator) throws IOException {
        File file = new File(filePath);
        Writer writer = new FileWriter(file, Charset.forName("GBK"));
        return new CSVWriter(writer, separator);
    }

    private String[] getHeader(List<Map<String, Object>> maps) {
        Set<String> columnName = new HashSet<>();
        for (Map<String, Object> map : maps) {
            columnName = map.keySet();
            break;
        }
        return columnName.toArray(new String[0]);
    }

    private List<String[]> getCsvContent(List<Map<String, Object>> inputRowList) {
        List<String[]> resultRowList = new ArrayList<>();

        for (Map<String, Object> inputRowColumnMap : inputRowList) {
            List<String> resultRow = new ArrayList<>();
            for (String key : inputRowColumnMap.keySet()) {
                Object value = inputRowColumnMap.get(key);
                if (value == null) {
                    value = StringUtils.EMPTY;
                }

                if (value instanceof Date) {
                    Date date = (Date) value;
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                    String datetimeStr = localDateTime.format(DEFAULT_DATE_TIME_FORMAT);
                    resultRow.add(datetimeStr);
                } else if (value instanceof String) {
                    resultRow.add((String) value);
                }
            }
            resultRowList.add(resultRow.toArray(new String[0]));
        }
        return resultRowList;
    }
}
