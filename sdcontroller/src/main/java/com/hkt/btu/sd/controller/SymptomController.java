package com.hkt.btu.sd.controller;


import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/symptom")
public class SymptomController {

    @Resource(name = "sdSymptomFacade")
    SdSymptomFacade sdSymptomFacade;

    @GetMapping("/create-symptom")
    public String showCreateSymptom(Model model) {
        model.addAttribute("symptomGroupList", sdSymptomFacade.getSymptomGroupList());
        return "symptom/createSymptom";
    }

    @PostMapping("/post-create-symptom")
    public ResponseEntity<?> createSymptom(@RequestParam String symptomCode,@RequestParam String symptomGroupCode,@RequestParam String symptomDescription) {
        String errorMsg = sdSymptomFacade.createSymptom(symptomCode,symptomGroupCode,symptomDescription);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/create-symptom-mapping")
    public String showCreateSymptomMapping(Model model) {
        model.addAttribute("serviceTypeList", sdSymptomFacade.getServiceTypeList());
        model.addAttribute("symptomGroupList", sdSymptomFacade.getSymptomGroupList());
        return "symptom/createSymptomMapping";
    }

    @PostMapping("/post-create-symptom-mapping")
    public ResponseEntity<?> createSymptomMapping(@RequestParam String serviceTypeCode,@RequestParam String symptomGroupCode) {
        String errorMsg = sdSymptomFacade.createSymptomMapping(serviceTypeCode,symptomGroupCode);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/edit-symptom-mapping")
    public String showEditSymptomMapping(@RequestParam String serviceTypeCode,@RequestParam String symptomGroupCode,Model model) {
        model.addAttribute("serviceTypeList", sdSymptomFacade.getServiceTypeList());
        model.addAttribute("symptomGroupList", sdSymptomFacade.getSymptomGroupList());
        model.addAttribute("serviceTypeCode", serviceTypeCode);
        model.addAttribute("symptomGroupCode", symptomGroupCode);
        return "symptom/editSymptomMapping";
    }

    @PostMapping("/post-edit-symptom-mapping")
    public ResponseEntity<?> editSymptomMapping(@RequestParam String oldServiceTypeCode,@RequestParam String oldSymptomGroupCode,@RequestParam String serviceTypeCode,@RequestParam String symptomGroupCode) {
        String errorMsg = sdSymptomFacade.editSymptomMapping(oldServiceTypeCode,oldSymptomGroupCode,serviceTypeCode,symptomGroupCode);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }
}
