package com.hkt.btu.sd.controller;

import com.hkt.btu.common.facade.data.BtuCodeDescData;
import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.*;
import com.hkt.btu.sd.facade.data.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/ticket")
@Controller
public class TicketController {
    private static final Logger LOG = LogManager.getLogger(TicketController.class);

    @Resource(name = "ticketFacade")
    SdTicketFacade ticketFacade;
    @Resource(name = "userRoleFacade")
    SdUserRoleFacade userRoleFacade;
    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;
    @Resource(name = "userFacade")
    SdUserFacade userFacade;
    @Resource(name = "serviceTypeUserRoleFacade")
    SdServiceTypeUserRoleFacade serviceTypeUserRoleFacade;
    @Resource(name = "sdSymptomFacade")
    SdSymptomFacade symptomFacade;

    @PostMapping("contact/update")
    public ResponseEntity<?> updateContactInfo(@RequestBody List<SdTicketContactData> contactList) {
        try {
            contactList.stream().map(SdTicketContactData::getTicketMasId).findFirst().ifPresent(ticketMasId -> {
                ticketFacade.isAllow(ticketMasId, SdTicketMasData.ACTION_TYPE.COMPLETE);
            });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        String errorMsg = ticketFacade.updateContactInfo(contactList);
        return ResponseEntity.ok(errorMsg);
    }

    @GetMapping("/contact")
    public ResponseEntity<?> getContactInfo(@RequestParam Integer ticketMasId) {
        List<SdTicketContactData> data = ticketFacade.getContactInfo(ticketMasId);
        return ResponseEntity.ok(data);
    }

    @GetMapping("ajax-search-ticket-remarks")
    public ResponseEntity<?> ajaxSearchTicketRemarks(@RequestParam Integer ticketMasId) {
        List<SdTicketRemarkData> dataList = ticketFacade.getTicketRemarksByTicketId(ticketMasId);
        return ResponseEntity.ok(dataList);
    }

    @PostMapping("/post-create-ticket-remarks")
    public ResponseEntity<?> createTicketRemarks(@RequestParam Integer ticketMasId, @RequestParam String remarks) {
        try {
            ticketFacade.isAllow(ticketMasId, SdTicketMasData.ACTION_TYPE.COMPLETE);
            ticketFacade.createTicketRemarks(ticketMasId, remarks);
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/ajax-get-ticket")
    public ResponseEntity<?> getTicketInfo(@RequestParam Integer ticketMasId) {
        SdTicketData ticketData = ticketFacade.getTicketInfo(ticketMasId);
        return ResponseEntity.ok(ticketData);
    }

    @ResponseBody
    @GetMapping("/exportExcel")
    public ResponseEntity<?> downloadRequest(@RequestParam Map<String, String> searchFormData) throws IOException {
        List<SdTicketExportData> accessRequestDataList = ticketFacade.searchTicketListForExport(searchFormData);
        if (CollectionUtils.isEmpty(accessRequestDataList)) {
            return ResponseEntity.badRequest().body("Ticket data not found.");
        } else if (accessRequestDataList.size() > 10000) {
            return ResponseEntity.badRequest().body("Ticket data exceeds 10000 row(s).");
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        ticketFacade.fillSheet(workbook.createSheet("Tickets"), accessRequestDataList);
        String fileName = ticketFacade.getFileName();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        baos.close();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=\"%s", fileName));
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE))
                .<Object>body(baos.toByteArray());
    }

    @GetMapping("dashboard")
    public String dashboard(Model model) {
        List<SdOutstandingFaultData> outstandingFaultList = ticketFacade.getOutstandingFault();
        List<SdTicketTimePeriodSummaryData> ticketTimePeriodSummaryList = ticketFacade.getTicketTimePeriodSummary();
        String avgFaultCleaningTime = ticketFacade.getAvgFaultCleaningTime();
        if (CollectionUtils.isNotEmpty(outstandingFaultList)) {
            model.addAttribute("outstandingFaultList", outstandingFaultList);
        }
        if (CollectionUtils.isNotEmpty(ticketTimePeriodSummaryList)) {
            model.addAttribute("ticketTimePeriodSummaryList", ticketTimePeriodSummaryList);
        }
        if (StringUtils.isNotBlank(avgFaultCleaningTime)) {
            model.addAttribute("avgFaultCleaningTime", avgFaultCleaningTime);
        }
        return "ticket/dashboard";
    }

    @GetMapping("/create-ticket")
    public String createTicket() {
        return "ticket/createTicket";
    }

    @PostMapping("submit")
    public ResponseEntity<?> createTicket(@RequestBody List<SdCreateTicketData> formDataList) {
        try {
            SdCreateTicketData formData = formDataList.stream().findFirst().get();
            String serviceNumber = formData.getServiceNumber();
            String ticketType = formData.getTicketType();
            Integer priority = formData.getPriority();
            List<SdTicketContactData> contactList = formData.getContactList();
            String remarks = formData.getRemarks();
            int ticketMasId = ticketFacade.createTicket(serviceNumber, ticketType, priority, contactList, remarks);
            return ResponseEntity.ok(ticketMasId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ModelAndView showQueryTicket(int ticketMasId) {
        return ticketFacade.getTicket(ticketMasId).map(data -> {
            ModelAndView modelAndView = new ModelAndView("ticket/ticketInfo");
            modelAndView.addObject("ticketInfo", data);
            return modelAndView;
        }).orElse(new ModelAndView("redirect:/ticket/search-ticket"));
    }

    @PostMapping("close")
    public ResponseEntity<?> ticketClose(int ticketMasId, String reasonContent) {
        String errorMsg = ticketFacade.closeTicket(ticketMasId, reasonContent);
        if (StringUtils.isEmpty(errorMsg)) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }

    @GetMapping("/search-ticket")
    public String searchTicket(Model model) {
        List<BtuCodeDescData> ticketStatusList = ticketFacade.getTicketStatusList();
        if (CollectionUtils.isNotEmpty(ticketStatusList)) {
            model.addAttribute("ticketStatusList", ticketStatusList);
        }

        List<BtuCodeDescData> ticketTypeList = ticketFacade.getTicketTypeList();
        if (CollectionUtils.isNotEmpty(ticketTypeList)) {
            model.addAttribute("ticketTypeList", ticketTypeList);
        }

        return "ticket/searchTicket";
    }

    @GetMapping("/searchTicket")
    public ResponseEntity<?> searchTicket(@RequestParam(defaultValue = "0") int draw,
                                          @RequestParam(defaultValue = "0") int start,
                                          @RequestParam(defaultValue = "10") int length,
                                          @RequestParam Map<String, String> searchFormData) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<SdTicketMasData> pageData = ticketFacade.searchTicketList(pageable, searchFormData);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }

    @GetMapping("/getAssignEngineer")
    public ResponseEntity<?> getAssignEngineer(@RequestParam String roleId) {
        List<SdUserData> userDataList = userFacade.getUserByRoleId(roleId);
        if (CollectionUtils.isEmpty(userDataList)) {
            return ResponseEntity.badRequest().body("Assign Engineer not found.");
        } else {
            return ResponseEntity.ok(userDataList);
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<?> ticketAssign(int ticketMasId, String engineer, String remark) {
        String errorMsg = ticketFacade.assignTicket(ticketMasId, engineer, remark);
        if (StringUtils.isEmpty(errorMsg)) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<?> ticketComplete(int ticketMasId, String remark) {
        String errorMsg = ticketFacade.completeTicket(ticketMasId, remark);
        if (StringUtils.isEmpty(errorMsg)) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }
}
