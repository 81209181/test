package com.hkt.btu.sd.core.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.List;

public class POIExcelWriter {
    public POIExcelWriter() {
    }

    public static void appendRetrievedData(HSSFSheet sheet, String[] dateFieldNameArray, List<List<String>> dbTableRowArrayList) {
        if (dbTableRowArrayList == null) {
            appendRetrievedDataList(sheet, dateFieldNameArray, null);
        } else {
            List<List<String>> dbTableRowList = new ArrayList<>(dbTableRowArrayList);
            appendRetrievedDataList(sheet, dateFieldNameArray, dbTableRowList);
        }
    }


    public static void appendRetrievedDataList(HSSFSheet sheet, String[] dateFieldNameArray, List<List<String>> dbTableRowList) {
        // Start at row 0 for new sheet. Otherwise, start a new row by adding 1.
        int startRowIndex = sheet.getLastRowNum() == 0 ? 0 : sheet.getLastRowNum() + 1;

        if (dateFieldNameArray != null) {
            HSSFRow row = sheet.createRow(startRowIndex);
            startRowIndex++;

            for (int j = 0; j < dateFieldNameArray.length; j++) {
                HSSFCell cell = row.createCell(j);
                // Title row field
                cell.setCellValue(new HSSFRichTextString(dateFieldNameArray[j]));
            }
        }

        for (int i = 0; i < dbTableRowList.size(); i++) {
            // Process a database table row
            HSSFRow row = sheet.createRow(startRowIndex + i);
            List<String> row_field_content_str_array_list = dbTableRowList.get(i);

            for (int j = 0; j < row_field_content_str_array_list.size(); j++) {
                // Process a database table row field
                HSSFCell cell = row.createCell(j);

                // if (i == 0 && data_field_name_array != null)
                // cell.setCellValue(new HSSFRichTextString(data_field_name_array[j])); // Title row field
                // Data row field
                cell.setCellValue(new HSSFRichTextString(row_field_content_str_array_list.get(j)));
            }
        }

        // Resize column
        if (dateFieldNameArray != null) {
            for (int x = 0; x < dateFieldNameArray.length; x++) {
                sheet.autoSizeColumn(x);
            }
        }
    }

    public static void appendRetrievedDataList(XSSFSheet sheet, String[] dateFieldNameArray, List<List<String>> dbTableRowList) {
        // Start at row 0 for new sheet. Otherwise, start a new row by adding 1.
        int startRowIndex = sheet.getLastRowNum() == 0 ? 0 : sheet.getLastRowNum() + 1;

        if (dateFieldNameArray != null) {
            XSSFRow row = sheet.createRow(startRowIndex);
            startRowIndex++;

            for (int j = 0; j < dateFieldNameArray.length; j++) {
                XSSFCell cell = row.createCell(j);
                // Title row field
                cell.setCellValue(new XSSFRichTextString(dateFieldNameArray[j]));
            }
        }

        for (int i = 0; i < dbTableRowList.size(); i++) {
            // Process a database table row
            XSSFRow row = sheet.createRow(startRowIndex + i);
            List<String> row_field_content_str_array_list = dbTableRowList.get(i);

            for (int j = 0; j < row_field_content_str_array_list.size(); j++) {
                // Process a database table row field
                XSSFCell cell = row.createCell(j);
                // Data row field
                cell.setCellValue(new XSSFRichTextString(row_field_content_str_array_list.get(j)));
            }
        }

        // Resize column
        if (dateFieldNameArray != null) {
            for (int x = 0; x < dateFieldNameArray.length; x++) {
                sheet.autoSizeColumn(x);
            }
        }
    }
}