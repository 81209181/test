package com.hkt.btu.sd.controller;


import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/createSymptom")
    public ResponseEntity<?> createSymptom(@RequestParam String symptomCode,@RequestParam String symptomGroupCode,@RequestParam String symptomDescription) {
        if(sdSymptomFacade.createSymptom(symptomCode,symptomGroupCode,symptomDescription)){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, "Create failed."));
        }
    }
}
