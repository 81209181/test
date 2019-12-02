package com.hkt.btu.sd.controller;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import com.hkt.btu.sd.facade.data.UpdateSymptomFormData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/symptom")
public class SymptomController {

    @Resource(name = "sdSymptomFacade")
    SdSymptomFacade sdSymptomFacade;
    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;

    @GetMapping("/create-symptom")
    public String showCreateSymptom(Model model) {
        List<SdSymptomData> symptomGroupList = sdSymptomFacade.getSymptomGroupList();
        if (CollectionUtils.isNotEmpty(symptomGroupList)) {
            model.addAttribute("symptomGroupList", symptomGroupList);
        }
        return "symptom/createSymptom";
    }

    @PostMapping("/post-create-symptom")
    public ResponseEntity<?> createSymptom(@RequestParam String symptomCode, @RequestParam String symptomGroupCode, @RequestParam String symptomDescription) {
        String errorMsg = sdSymptomFacade.createSymptom(symptomCode, symptomGroupCode, symptomDescription);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("search-symptom")
    public String showSearchSymptom() {
        return "symptom/searchSymptom";
    }

    @GetMapping("ajax-search-symptom")
    public ResponseEntity<?> ajaxSearchSymptom(@RequestParam(defaultValue = "0") int draw,
                                               @RequestParam(defaultValue = "0") int start,
                                               @RequestParam(defaultValue = "10") int length,
                                               @RequestParam String symptomGroupCode,
                                               @RequestParam String symptomDescription,
                                               @RequestParam String dirList,
                                               @RequestParam String sortList) {
        int page = start / length;

        Pageable pageable = PageRequest.of(page, length);

        PageData<SdSymptomData> pageData = sdSymptomFacade.searchSymptomList(pageable, symptomGroupCode, symptomDescription, dirList, sortList);
        return ResponseEntityHelper.buildDataTablesResponse(draw, pageData);
    }

    @GetMapping("/edit-symptom-mapping")
    public String showEditSymptomMapping(@RequestParam String symptomCode, Model model, final RedirectAttributes redirectAttributes) {

        if (StringUtils.isNotEmpty(symptomCode)) {
            model.addAttribute("symptomCode", symptomCode);

            EditResultData result = sdSymptomFacade.getSymptomMapping(symptomCode);
            String errorMsg = result == null ? "" : result.getErrorMsg();
            List<String> symptomMapping = result == null ? null : (List<String>) result.getList();
            if (CollectionUtils.isNotEmpty(symptomMapping)) {
                model.addAttribute("symptomMappingList", symptomMapping);
            } else {
                redirectAttributes.addFlashAttribute(PageMsgController.ERROR_MSG, errorMsg);
            }
        }

        List<SdSymptomData> symptomGroupList = sdSymptomFacade.getSymptomGroupList();
        if (CollectionUtils.isNotEmpty(symptomGroupList)) {
            model.addAttribute("symptomGroupList", symptomGroupList);
        }

        List<SdServiceTypeData> serviceTypeList = serviceTypeFacade.getServiceTypeList();
        if (CollectionUtils.isNotEmpty(serviceTypeList)) {
            model.addAttribute("serviceTypeList", serviceTypeList);
        }

        return "symptom/editSymptomMapping";
    }

    @GetMapping("/edit-symptom-mapping/get-symptom")
    public ResponseEntity<?> ajaxGetSymptom(@RequestParam String symptomCode) {
        if (symptomCode == null) {
            return ResponseEntity.badRequest().body("Empty Symptom Code!");
        }

        SdSymptomData symptomData = sdSymptomFacade.getSymptomBySymptomCode(symptomCode);

        if (symptomData == null) {
            return ResponseEntity.badRequest().body("Symptom not found.");
        } else {
            return ResponseEntity.ok(symptomData);
        }
    }

    @PostMapping("/post-edit-symptom-mapping")
    public ResponseEntity<?> editSymptomMapping(@RequestBody UpdateSymptomFormData symptomFormData) {
        String errorMsg = sdSymptomFacade.editSymptomMapping(symptomFormData);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

}
