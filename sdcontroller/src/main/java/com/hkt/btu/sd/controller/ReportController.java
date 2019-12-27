package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdSqlReportFacade;
import com.hkt.btu.sd.facade.data.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping(value = "/system/report")
public class ReportController {

    @Resource(name = "reportFacade")
    SdSqlReportFacade sqlReportFacade;

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

    @GetMapping("/ajax-list-report-job-inst")
    public ResponseEntity<?> ajaxListSqlReportJobInst() {
        List<SdCronJobInstData> reportJobInstance = sqlReportFacade.getAllReportJobInstance();

        if (reportJobInstance == null) {
            return ResponseEntity.badRequest().body("Report Job inst list not found.");
        } else {
            return ResponseEntity.ok(reportJobInstance);
        }
    }

    @PostMapping("/ajax-resume-report")
    public ResponseEntity<?> ajaxResumeReport(@RequestParam String keyName) {
        String errorMsg = sqlReportFacade.resumeReport(keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }


    @PostMapping("/ajax-pause-report")
    public ResponseEntity<?> ajaxPauseReport(@RequestParam String keyName) {
        String errorMsg = sqlReportFacade.pauseReport(keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/ajax-trigger-report")
    public ResponseEntity<?> ajaxTriggerJob(@RequestParam String keyName) {
        String errorMsg = sqlReportFacade.triggerReport(keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/create")
    public String showCreateConfigParam(@ModelAttribute("requestReportData") RequestReportData data) {
        return "system/report/createSqlReport";
    }

    @PostMapping("/createSqlReport")
    public String createSqlReport(final RedirectAttributes redirectAttributes,
                                  RequestReportData data) {
        ResponseReportData response = sqlReportFacade.createReport(data);
        String errorMsg = response == null ? "No create result" : response.getErrorMsg();
        if (response.getReportId() == null) {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("requestReportData", data);
            return "redirect:/system/report/create";
        } else {
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Created Report.");
            return "redirect:/system/report/edit-sql-report?reportId=" + response.getReportId();
        }
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
        ResponseReportData response = sqlReportFacade.updateReport(data);
        if (response.getErrorMsg() == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, response.getErrorMsg()));
        }
    }

    @PostMapping("ajax-delete-report")
    public ResponseEntity<?> deleteReport(@RequestParam String reportId) {
        ResponseReportData response = sqlReportFacade.deleteReport(reportId);
        String errorMsg = response == null ? "Delete failed." : response.getErrorMsg();
        if (response != null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/ajax-active-report")
    public ResponseEntity<?> ajaxActiveReport(@RequestParam String reportName) {
        String errorMsg = sqlReportFacade.activeReport(reportName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/ajax-deactivate-report")
    public ResponseEntity<?> ajaxDeactiveReport(@RequestParam String reportName) {
        String errorMsg = sqlReportFacade.deactiveReport(reportName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/ajax-sync-report")
    public ResponseEntity<?> ajaxSyncJob(@RequestParam String reportName) {
        String errorMsg = sqlReportFacade.syncReport(reportName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> ajaxListFile(@RequestParam String reportId) {
        List<SdReportHistoryData> fileList = sqlReportFacade.getFileList(reportId);
        if (CollectionUtils.isEmpty(fileList)) {
            return ResponseEntity.badRequest().body("get file list failed.");
        } else {
            return ResponseEntity.ok(fileList);
        }
    }

    @ResponseBody
    @GetMapping("/downLoadReport")
    public ResponseEntity<?> downLoadReport(String reportId, String reportName) {
        org.springframework.core.io.Resource resource = sqlReportFacade.downLoadReport(reportId, reportName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=\"%s", reportName));
        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE))
                .body(resource);

        return responseEntity;
    }

}
