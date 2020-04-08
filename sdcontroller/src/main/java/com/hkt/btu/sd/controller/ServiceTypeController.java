package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.SdServiceTypeUserRoleFacade;
import com.hkt.btu.sd.facade.SdUserRoleFacade;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdUserRoleData;
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
@RequestMapping("/system/service-type")
public class ServiceTypeController {

    @Resource(name = "serviceTypeFacade")
    SdServiceTypeFacade serviceTypeFacade;
    @Resource(name = "userRoleFacade")
    SdUserRoleFacade userRoleFacade;
    @Resource(name = "serviceTypeUserRoleFacade")
    SdServiceTypeUserRoleFacade serviceTypeUserRoleFacade;

    @GetMapping("")
    public String redirectToServiceTypeMapping() {
        return "system/serviceType/listServiceType";
    }

    @GetMapping("/service-type-list")
    public ResponseEntity<?> getServiceTypeList() {
        List<SdServiceTypeData> serviceTypeList = serviceTypeFacade.getServiceTypeList();
        if (CollectionUtils.isEmpty(serviceTypeList)) {
            return ResponseEntity.badRequest().body("service type is empty.");
        } else {
            return ResponseEntity.ok(serviceTypeList);
        }
    }

    @GetMapping("/edit-service-type")
    public String redirectToEditServiceTypeMapping(@RequestParam String serviceTypeCode, Model model) {
        if (StringUtils.isNotEmpty(serviceTypeCode)) {
            model.addAttribute("serviceTypeCode", serviceTypeCode);
        }

        List<SdUserRoleData> allUserRoles = userRoleFacade.listAllUserRole();
        if (CollectionUtils.isNotEmpty(allUserRoles)) {
            model.addAttribute("allUserRole", userRoleFacade.filterUserRoleList(allUserRoles));
        }

        List<String> serviceTypeUserRoleList = serviceTypeUserRoleFacade.getServiceTypeUserRoleByServiceType(serviceTypeCode);
        if (CollectionUtils.isNotEmpty(serviceTypeUserRoleList)) {
            model.addAttribute("serviceTypeUserRoleList", serviceTypeUserRoleList);
        }

        return "system/serviceType/editServiceType";
    }

    @PostMapping("/edit-service-type-mapping")
    public ResponseEntity<?> editServiceTypeUserRole(@RequestParam String serviceType,
                                                      @RequestParam List<String> userRoleId) {
        String errorMsg = serviceTypeUserRoleFacade.editServiceTypeUserRole(serviceType, userRoleId);
        if(errorMsg==null){
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        }else {
            return ResponseEntity.ok(SimpleAjaxResponse.of(false, errorMsg));
        }
    }
}
