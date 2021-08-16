package com.hkt.btu.sd.controller;


import com.hkt.btu.common.facade.data.PageData;
import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.controller.response.helper.ResponseEntityHelper;
import com.hkt.btu.sd.core.service.bean.SdSymptomGroupBean;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.SdSymptomFacade;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.EditResultData;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdSymptomData;
import com.hkt.btu.sd.facade.data.SdSymptomWorkingPartyMappingData;
import com.hkt.btu.sd.facade.data.SdUpdateSymptomFormData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/symptom")
public class SymptomController {

    @Resource(name = "sdSymptomFacade")
    SdSymptomFacade sdSymptomFacade;
    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;
    @Resource(name = "userRoleFacade")
    SdUserRoleFacade userRoleFacade;

    @GetMapping("/create-symptom")
    public String showCreateSymptom(Model model) {
        List<SdSymptomData> symptomGroupList = sdSymptomFacade.getSymptomGroupList();
        List<SdServiceTypeData> serviceTypeList = serviceTypeFacade.getServiceTypeList();
        if (CollectionUtils.isNotEmpty(symptomGroupList)) {
            model.addAttribute("symptomGroupList", symptomGroupList);
        }
        if (CollectionUtils.isNotEmpty(serviceTypeList)) {
            model.addAttribute("serviceTypeList", serviceTypeList);
        }
        return "symptom/createSymptom";
    }

    @PostMapping("/post-create-symptom")
    public ResponseEntity<?> createSymptom(@RequestBody SdUpdateSymptomFormData symptomFormData) {
        if (StringUtils.isEmpty(symptomFormData.getSymptomGroupCode())) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false,"Empty Symptom Group Code."));
        } else if (StringUtils.isEmpty(symptomFormData.getSymptomDescription())) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false,"Empty Symptom Description."));
        } else if (StringUtils.isEmpty(symptomFormData.getVoiceLineTest())) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false,"Empty Voice Line Test."));
        } else if (StringUtils.isEmpty(symptomFormData.getApptMode())) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false,"Empty Appointment Mode."));
        }

        try {
            String res = sdSymptomFacade.createSymptom(symptomFormData.getSymptomGroupCode(),
                    symptomFormData.getSymptomDescription(), symptomFormData.getServiceTypeList(),
                    symptomFormData.getVoiceLineTest(), symptomFormData.getApptMode());
            return ResponseEntity.ok(SimpleAjaxResponse.of(true, res));
        } catch (DuplicateKeyException e){
            return ResponseEntity.ok(SimpleAjaxResponse.of(false,"Symptom Code already exists."));
        }
    }

    @PostMapping("/ifSymptomDescExist")
    public ResponseEntity<?> ifSymptomDescExist(@RequestBody SdSymptomData symptomData){
        if (StringUtils.isEmpty(symptomData.getSymptomGroupCode())){
            return ResponseEntity.ok(SimpleAjaxResponse.of(false,"Empty Symptom Group Code."));
        } else if (StringUtils.isEmpty(symptomData.getSymptomDescription())) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false,"Empty Symptom Description."));
        }

        if (sdSymptomFacade.ifSymptomDescExist(symptomData)) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(true, "warningMsg"));
        }

        return ResponseEntity.ok(SimpleAjaxResponse.of());
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
    public ResponseEntity<?> editSymptomMapping(@RequestBody SdUpdateSymptomFormData symptomFormData) {
        String errorMsg = sdSymptomFacade.editSymptomMapping(symptomFormData);
        if (errorMsg == null) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }

    @GetMapping("/symptom-group")
    public String symptomGroup(Model model){
        List<SdUserRoleData> allUserRole = userRoleFacade.listAllUserRole();
        model.addAttribute("allUserRole", userRoleFacade.filterUserRoleList(allUserRole));
        return "symptom/symptomGroup";
    }

    @GetMapping("/symptom-group/list")
    public ResponseEntity<?> getSymptomGroupList() {
        List<SdSymptomData> symptomGroupList = sdSymptomFacade.getSymptomGroupList();
        return ResponseEntity.ok(symptomGroupList);
    }

    @PostMapping("/symptom-group/create")
    public ResponseEntity<?> createSymptpmGroup(@RequestParam String symptomGroupCode, @RequestParam String symptomGroupName, @RequestParam(required = false) List<String> roleList){
        String errmsg = sdSymptomFacade.createSymptomGroup(symptomGroupCode, symptomGroupName, roleList);
        if (null == errmsg){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errmsg));
        }
    }

    @GetMapping("/symptom-group/get")
    public ResponseEntity<?> getSymptomGroup(String symptomGroupCode){
        if (StringUtils.isEmpty(symptomGroupCode)){
            return ResponseEntity.badRequest().body("Empty Symptom Group Code.");
        }
        Optional<SdSymptomGroupBean> bean = sdSymptomFacade.getSymptomGroup(symptomGroupCode);
        if (bean.isEmpty()) {
            return ResponseEntity.badRequest().body("SYMPTOM_GROUP not found.");
        }
        return ResponseEntity.ok(bean);
    }

    @PostMapping("/symptom-group/edit")
    public ResponseEntity<?> editSymptomGroup(@RequestParam String symptomGroupCode, @RequestParam String symptomGroupName, @RequestParam(required = false) List<String> roleList) {
        String errorMsg = sdSymptomFacade.updateSymptomGroup(symptomGroupCode, symptomGroupName, roleList);
        if (null != errorMsg) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(true, "Symptom group code:"+symptomGroupCode+" update success."));
        }
    }

    @PostMapping("/symptom-group/delete")
    public ResponseEntity<?> delSymptomGroup(String symptomGroupCode) {
        String errMsg = sdSymptomFacade.delSymptomGroup(symptomGroupCode);
        if (null != errMsg) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errMsg));
        } else {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }
    }

    @GetMapping("/symptom-workingparty-mapping")
    public String symptomWorkingpargy(Model model) {
        List<SdServiceTypeData> serviceTypeList = serviceTypeFacade.getServiceTypeList();
        List<SdSymptomData> allSymptomList = sdSymptomFacade.getAllSymptomList();
        model.addAttribute("allSymptomList", allSymptomList);
        model.addAttribute("serviceTypeList", serviceTypeList);
        return "symptom/symptomWorkingPartyMapping";
    }

    @GetMapping("/symptom-workingparty-mapping/list")
    public ResponseEntity<?> getSymptomWorkingPartyMappingList(){
        return ResponseEntity.ok(sdSymptomFacade.getSymptomWorkingPartyMappingList());
    }

    @PostMapping("/symptom-workingparty-mapping/create")
    public ResponseEntity<?> createSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode) {
        String errMsg = sdSymptomFacade.createSymptomWorkingPartyMapping(symptomCode, workingParty, serviceTypeCode);
        if (null != errMsg) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errMsg));
        }
        return ResponseEntity.ok(SimpleAjaxResponse.of(true, "SYMPTOM_WORKINGPARTY_MAPPING (symptom code: "+symptomCode+") create success."));
    }

    @PostMapping("/symptom-workingparty-mapping/edit")
    public ResponseEntity<?> updateSymptomWorkingPartyMapping(String symptomCode, String workingParty, String serviceTypeCode) {
        String errMsg = sdSymptomFacade.updateSymptomWorkingPartyMapping(symptomCode, workingParty, serviceTypeCode);
        if (null != errMsg) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errMsg));
        }
        return ResponseEntity.ok(SimpleAjaxResponse.of(true,"SYMPTOM_WORKINGPARTY_MAPPING (symptom code: "+symptomCode+") update success."));
    }

    @GetMapping("/symptom-workingparty-mapping/get")
    public ResponseEntity<?> getSymptomWorkingPartyMapping(String symptomCode) {
        if (StringUtils.isEmpty(symptomCode)){
            return ResponseEntity.badRequest().body("Empty Symptom Code.");
        }
        SdSymptomWorkingPartyMappingData data = sdSymptomFacade.getSymptomWorkingPartyMapping(symptomCode);
        if (null != data) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.badRequest().body("SYMPTOM_WORKINGPARTY_MAPPING (symptom code: "+symptomCode+") not found.");
    }

    @PostMapping("/symptom-workingparty-mapping/delete")
    public ResponseEntity<?> delSymptomWorkingPartyMapping(String symptomCode) {
        String errMsg = sdSymptomFacade.delSymptomWorkingPartyMapping(symptomCode);
        if (null != errMsg) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errMsg));
        }
        return ResponseEntity.ok(SimpleAjaxResponse.of(true, "SYMPTOM_WORKINGPARTY_MAPPING (symptom code: "+symptomCode+") delete success."));
    }

}
