package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.facade.SdSqlReportFacade;
import com.hkt.btu.sd.facade.data.RequestReportData;
import com.hkt.btu.sd.facade.data.SdSqlReportData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/system/report")
public class ReportController {

    @Resource(name = "reportFacade")
    SdSqlReportFacade sqlReportFacade;


    @GetMapping({"", "/", "/index"})
    public String index() {
        return "redirect:search";
    }

    @GetMapping({"/search"})
    public String searchSqlReport() {
        return "system/report/searchSqlReport";
    }

    @GetMapping("/ajax-list-sql-report")
    public ResponseEntity<?> ajaxListSqlReport() {
        List<SdSqlReportData> sqlReportData = sqlReportFacade.getAllReportData();

        if (CollectionUtils.isEmpty(sqlReportData)) {
            return ResponseEntity.badRequest().body("Sql Report list not found.");
        } else {
            return ResponseEntity.ok(sqlReportData);
        }
    }

    @GetMapping("/create")
    public String showCreateConfigParam(Model model) {
        return "system/report/createSqlReport";
    }

    @PostMapping("/createSqlReport")
    public ResponseEntity<?> createSqlReport(RequestReportData data) {
        sqlReportFacade.createReport(data);
        return ResponseEntity.ok().body("SQL Report create success.");
    }

    @GetMapping("/edit-sql-report")
    public String editUserRole(@RequestParam String reportId, Model model) {
        if (StringUtils.isNotEmpty(reportId)) {
            SdSqlReportData data = sqlReportFacade.getSqlReportDataByReportId(reportId);
            model.addAttribute("data", data);
        }
        return "system/report/editSqlReport";
    }

    @PostMapping("/updateSqlReport")
    public ResponseEntity<?> updateSqlReport(RequestReportData data) {
        return ResponseEntity.ok().body("SQL Report update success.");
    }

    @PostMapping("/ajax-delete-report")
    public ResponseEntity<?> deleteReport(@RequestParam String reportName) {
        return ResponseEntity.ok().body("SQL Report delete success.");
    }
}
