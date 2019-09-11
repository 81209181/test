package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.data.SdSqlReportData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/report")
public class ReportController {

    @GetMapping({"", "/", "/index"})
    public String index() {
        return "redirect:/report/search";
    }

    @GetMapping({"/search"})
    public String searchSqlReport() {
        return "report/searchSqlReport";
    }

    @GetMapping("/ajax-list-sql-report")
    public ResponseEntity<?> ajaxListSqlReport() {
        List<SdSqlReportData> sqlReportDataList = new ArrayList<>();

        // fix data
        SdSqlReportData sqlReportData = new SdSqlReportData();
        sqlReportData.setReportName("test");
        sqlReportData.setCronExp("0/20 * * * * ? ");
        sqlReportData.setSql("SELECT * FROM USER_PROFILE");
        sqlReportData.setExportTo("D:/");
        sqlReportData.setActive(true);
        sqlReportDataList.add(sqlReportData);

        if (sqlReportDataList == null) {
            return ResponseEntity.badRequest().body("Sql Report list not found.");
        } else {
            return ResponseEntity.ok(sqlReportDataList);
        }
    }

    @GetMapping("create")
    public String showCreateConfigParam(Model model) {
        return "report/createSqlReport";
    }

    @PostMapping("createSqlReport")
    public ResponseEntity<?> createSqlReport(@RequestParam String reportName, @RequestParam String cronExp,
                                             @RequestParam String sql,@RequestParam String status,
                                             @RequestParam String exportTo,@RequestParam String emailTo){
        return ResponseEntity.ok().body("SQL Report create success.");
    }

    @GetMapping("/edit-sql-report")
    public String editUserRole(@RequestParam String reportName, Model model) {
        if (StringUtils.isNotEmpty(reportName)) {
            model.addAttribute("reportName", reportName);
        }
        return "report/editSqlReport";
    }

    @PostMapping("updateSqlReport")
    public ResponseEntity<?> updateSqlReport(@RequestParam String reportName, @RequestParam String cronExp,
                                             @RequestParam String sql,@RequestParam String status,
                                             @RequestParam String exportTo,@RequestParam String emailTo){
        return ResponseEntity.ok().body("SQL Report update success.");
    }

    @PostMapping("ajax-delete-report")
    public ResponseEntity<?> deleteReport(@RequestParam String reportName){
        return ResponseEntity.ok(SimpleAjaxResponse.of());
    }
}
