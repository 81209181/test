package com.hkt.btu.noc.controller;

import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.noc.controller.response.SimpleAjaxResponse;
import com.hkt.btu.noc.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.noc.facade.NocCompanyFacade;
import com.hkt.btu.noc.facade.data.CreateResultData;
import com.hkt.btu.noc.facade.data.NocCompanyData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping(value = "/admin/manage-company")
public class ManageCompanyController {

    @Resource(name = "companyFacade")
    NocCompanyFacade nocCompanyFacade;



    @GetMapping("/create-company")
    public String createCompanyForm ( @ModelAttribute("inputCompData") NocCompanyData inputCompData ) {
        return "admin/manageCompany/createCompanyForm";
    }

    @PostMapping("/create-company")
    public String createCompanyForm ( final RedirectAttributes redirectAttributes,
            @ModelAttribute("inputCompData") NocCompanyData inputCompanyData) {
        CreateResultData createResultData = nocCompanyFacade.createCompany(inputCompanyData);
        Integer newCompanyId = createResultData==null ? null : createResultData.getNewId();
        String errorMsg = createResultData==null ? "No create result." : createResultData.getErrorMsg();

        if(newCompanyId==null){
            redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            redirectAttributes.addFlashAttribute("inputCompData", inputCompanyData);
            return "redirect:create-company";
        } else {
            redirectAttributes.addFlashAttribute(PageMsgController.INFO_MSG, "Company created.");
            return "redirect:edit-company?companyId="+newCompanyId;
        }
    }

    @GetMapping("/edit-company")
    public String editCompanyForm ( final Model model,
            @RequestParam Integer companyId,
            @ModelAttribute("editCompanyData") NocCompanyData editCompanyData) {
        if(companyId==null){
            model.addAttribute("", "Empty company ID!");
        }

        NocCompanyData nocCompanyData = nocCompanyFacade.getCompanyId(companyId);
        if(nocCompanyData==null){
            model.addAttribute("", "Empty company ID!");
        } else {
            model.addAttribute("editCompanyData", nocCompanyData);
        }

        return "admin/manageCompany/editCompanyForm";
    }

    @PostMapping("/edit-company")
    public ResponseEntity<?> editUserForm (@Valid @RequestBody NocCompanyData editCompanyData) {
        String errorMsg = nocCompanyFacade.editCompany(editCompanyData);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/search-company")
    public String searchCompany() {
        return "admin/manageCompany/searchCompany";
    }

    @GetMapping("/ajax-search-company")
    public ResponseEntity<?> ajaxSearchCompany(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required= false) Integer companyId,
            @RequestParam(required= false) String name) {
        int page = start/length;
        Pageable pageable = PageRequest.of(page, length);

        PageData<NocCompanyData> pageData = nocCompanyFacade.searchCompany(pageable, companyId, name);
        if(pageData==null){
            return ResponseEntity.badRequest().body("Null page data.");
        } else if(!StringUtils.isEmpty(pageData.getErrorMsg())){
            return ResponseEntity.badRequest().body(pageData.getErrorMsg());
        }

        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }



}
