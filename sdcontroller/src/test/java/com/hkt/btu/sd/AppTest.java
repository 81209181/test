package com.hkt.btu.sd;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.hkt.btu.sd.core.dao.mapper.SdConfigParamMapper;
import com.hkt.btu.sd.core.dao.mapper.SdUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    @Autowired
    SdConfigParamMapper configParamMapper;

    @Test
    public void shouldAnswerWithTrue() {
        List<String> entities = configParamMapper.getConfigGroupList();
        for (String entity : entities) {
            System.out.println(entity);
        }
    }

    @Test
    public void testStringUtils() {
        System.out.println(StringUtils.join("config key", " already exists."));
    }

    @Autowired
    SdUserMapper sdUserMapper;

    private TransactionTemplate transactionTemplate;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Before
    public void loadBean() {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Test
    public void testQueryColumnName() {
        try (CSVWriter csvWriter = prepareCSVWriter("D:\\test.csv", ',')) {
            String sql = "SELECT * FROM USER_PROFILE";
            //sql = sqlFilter(sql.toLowerCase());
            List<Map<String, Object>> maps = new ArrayList<>();
            maps = transactionTemplate.execute(txStatus ->{
                txStatus.setRollbackOnly();
                //return sdUserMapper.querySQL(sql);
                return null;
            });
            String[] header = getHeader(maps);
            csvWriter.writeNext(header);
            writeContent(csvWriter, maps);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Invalid sql statement.");
        }
    }

    private void writeContent(CSVWriter csvWriter, List<Map<String, Object>> maps) {
        List<String[]> contentResult = getCsvContent(maps);
        csvWriter.writeAll(contentResult);
    }

    private final String SQL_INJ_STR = "'|and|exec|insert|delete|update|\n" +
            "\n" +
            "|chr|mid|master|truncate|char|declare|;|-|+|,";


    public String sqlFilter(String sql) {
        if (StringUtils.isNotBlank(sql)) {
            sql = sql.replaceAll(SQL_INJ_STR, "");
        }
        return sql;
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
                if (value instanceof Timestamp) {
                    content.add(convertTimestamp((Date) value));
                }
            }
            contentResult.add(content.toArray(new String[content.size()]));
            content.clear();
        }
        return contentResult;
    }

    private String[] getHeader(List<Map<String, Object>> maps) {
        Set<String> column_name = new HashSet<>();
        for (Map<String, Object> map : maps) {
            column_name = map.keySet();
            break;
        }
        return column_name.toArray(new String[column_name.size()]);
    }

    private String convertTimestamp(Date timestamp) {
        String tsStr = "";
        //2019-08-20 18:01:21
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            tsStr = sdf.format(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    public CSVWriter prepareCSVWriter(String filePath, char separator) throws IOException {
        File file = new File(filePath);
        Writer writer = new FileWriter(file);
        CSVWriter csvWriter = new CSVWriter(writer, separator);
        return csvWriter;
    }
}
