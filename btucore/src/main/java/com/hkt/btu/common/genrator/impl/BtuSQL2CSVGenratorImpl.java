package com.hkt.btu.common.genrator.impl;

import au.com.bytecode.opencsv.CSVWriter;
import com.hkt.btu.common.genrator.BtuCSVGenrator;
import com.hkt.btu.common.genrator.BtuDateConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

public class BtuSQL2CSVGenratorImpl implements BtuCSVGenrator {

    private final char DEFAULT_SEPARATOR = ',';

    private static final Logger LOG = LogManager.getLogger(BtuSQL2CSVGenratorImpl.class);

    @Resource(name = "dateConverter")
    BtuDateConverter dateConverter;

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

            LOG.info("Generated CSV successfully!");
            File csvFile = new File(filePath);
            return csvFile;
        } catch (Exception e) {
            LOG.warn("Genrate CSV has error:" + e.getMessage());
        }
        return null;
    }

    private CSVWriter prepareCSVWriter(String filePath, char separator) throws IOException {
        File file = new File(filePath);
        Writer writer = new FileWriter(file);
        CSVWriter csvWriter = new CSVWriter(writer, separator);
        return csvWriter;
    }

    private String[] getHeader(List<Map<String, Object>> maps) {
        Set<String> column_name = new HashSet<>();
        for (Map<String, Object> map : maps) {
            column_name = map.keySet();
            break;
        }
        return column_name.toArray(new String[column_name.size()]);
    }

    private List<String[]> getCsvContent(List<Map<String, Object>> maps) {
        List<String[]> contentResult = new ArrayList<>();
        List<String> content = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value == null) {
                    value = "";
                }
                if (value instanceof String) {
                    content.add((String) value);
                }
                if (value instanceof Date) {
                    content.add(dateConverter.convert(value, null));
                }
            }
            contentResult.add(content.toArray(new String[content.size()]));
            content.clear();
        }
        return contentResult;
    }
}
