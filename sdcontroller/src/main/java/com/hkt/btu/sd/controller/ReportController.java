package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.BtuCronJobInstData;
import com.hkt.btu.common.facade.data.BtuReportFileData;
import com.hkt.btu.common.facade.data.BtuReportProfileData;
import com.hkt.btu.common.facade.data.BtuSimpleResponseData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdReportFacade;
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
import java.util.List;

@Controller
@RequestMapping(value = "/system/report")
public class ReportController {

    @Resource(name = "reportFacade")
    SdReportFacade reportFacade;

    @GetMapping({"/search"})
    public String searchSqlReport() {
        return "system/report/searchSqlReport";
    }

    @GetMapping("/ajax-list-sql-report")
    public ResponseEntity<?> ajaxListSqlReport() {
        List<BtuReportProfileData> reportProfileData = reportFacade.getAllReportProfiles();

        if (CollectionUtils.isEmpty(reportProfileData)) {
            return ResponseEntity.badRequest().body("Sql Report list not found.");
        } else {
            return ResponseEntity.ok(reportProfileData);
        }
    }

    @GetMapping("/ajax-list-report-job-inst")
    public ResponseEntity<?> ajaxListSqlReportJobInst() {
        List<BtuCronJobInstData> reportJobInstance = reportFacade.getAllReportJobInstance();

        if (reportJobInstance == null) {
            return ResponseEntity.badRequest().body("Report Job inst list not found.");
        } else {
            return ResponseEntity.ok(reportJobInstance);
        }
    }

    @PostMapping("/ajax-resume-report")
    public ResponseEntity<?> ajaxResumeReport(@RequestParam String keyName) {
        String errorMsg = reportFacade.resumeReport(keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }


    @PostMapping("/ajax-pause-report")
    public ResponseEntity<?> ajaxPauseReport(@RequestParam String keyName) {
        String errorMsg = reportFacade.pauseReport(keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/ajax-trigger-report")
    public ResponseEntity<?> ajaxTriggerJob(@RequestParam String keyName) {
        String errorMsg = reportFacade.triggerReport(keyName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/create")
    public String showCreateConfigParam(@ModelAttribute("requestReportData") BtuReportProfileData data) {
        return "system/report/createSqlReport";
    }

    @PostMapping("/createSqlReport")
    public String createSqlReport(final RedirectAttributes redirectAttributes,
                                  BtuReportProfileData data) {
        BtuSimpleResponseData response = reportFacade.createReport(data);
        String errorMsg = response == null ? "No create result" : response.getErrorMsg();
        if (response==null || StringUtils.isEmpty(response.getId())) {
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("requestReportData", data);
            return "redirect:/system/report/create";
        } else {
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Created Report.");
            return "redirect:/system/report/edit-sql-report?reportId=" + response.getId();
        }
    }

    @GetMapping("/edit-sql-report")
    public String editReportProfile(@RequestParam String reportId, Model model) {
        if (StringUtils.isNotEmpty(reportId)) {
            BtuReportProfileData data = reportFacade.getReportProfileById(reportId);
            model.addAttribute("data", data);
        }
        return "system/report/editSqlReport";
    }

    @PostMapping("/update-report")
    public ResponseEntity<?> updateSqlReport(BtuReportProfileData data) {
        BtuSimpleResponseData response = reportFacade.updateReportProfile(data);
        if (response.getErrorMsg() == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, response.getErrorMsg()));
        }
    }

    @PostMapping("ajax-delete-report")
    public ResponseEntity<?> deleteReport(@RequestParam String reportId) {
        BtuSimpleResponseData response = reportFacade.deleteReportProfile(reportId);
        String errorMsg = response == null ? "Delete failed." : response.getErrorMsg();
        if (response != null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/ajax-active-report")
    public ResponseEntity<?> ajaxActiveReport(@RequestParam String reportId) {
        String errorMsg = reportFacade.activateReportProfile(reportId);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/ajax-deactivate-report")
    public ResponseEntity<?> ajaxDeactiveReport(@RequestParam String reportId) {
        String errorMsg = reportFacade.deactivateReportProfile(reportId);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @PostMapping("/ajax-sync-report")
    public ResponseEntity<?> ajaxSyncJob(@RequestParam String reportName) {
        String errorMsg = reportFacade.syncReport(reportName);

        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> ajaxListFile(@RequestParam String reportId) {
        List<BtuReportFileData> fileList = reportFacade.getFileList(reportId);
        return ResponseEntity.ok(fileList);
    }

    @ResponseBody
    @GetMapping("/downloadReport")
    public ResponseEntity<?> downloadReport(String reportId, String reportFilename) {
        org.springframework.core.io.Resource resource = reportFacade.downloadReportFile(reportId, reportFilename);
        if (resource != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=\"%s", reportFilename));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE))
                    .<Object>body(resource);
        } else {
            return ResponseEntity.badRequest().body("download failed.");
        }
    }
}
