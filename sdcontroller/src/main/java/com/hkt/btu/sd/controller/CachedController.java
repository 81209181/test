package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdCachedFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/cached")
public class CachedController {

    @Resource(name = "cachedFacade")
    SdCachedFacade cachedFacade;

    @GetMapping({"","/"})
    public String forwardToCachedPage() {
        return "system/cachedObject/cachedObjectList";
    }

    @GetMapping("/list")
    public ResponseEntity<?> getCacheList() {
        List<String> cachedObjectList = List.of("ServiceType", "UserRole");
        return ResponseEntity.ok(cachedObjectList);
    }

    @GetMapping("reloadSiteConfig")
    public ResponseEntity<?> reloadSiteConfig() {
        boolean result = cachedFacade.reloadSiteConfigBean();
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(true,"Reload success."));
        } else {
            return ResponseEntity.badRequest().body(SimpleAjaxResponse.of(false,"Reload fail"));
        }
    }

    @GetMapping("reloadServiceTypeOfferMapping")
    public ResponseEntity<?> reloadServiceTypeOfferMapping() {
        boolean result = cachedFacade.reloadServiceTypeOfferMapping();
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(true,"Reload success."));
        } else {
            return ResponseEntity.badRequest().body(SimpleAjaxResponse.of(false,"Reload fail"));
        }
    }

    @GetMapping("reloadServiceTypeList")
    public ResponseEntity<?> reloadServiceTypeList() {
        boolean result = cachedFacade.reloadServiceTypeList();
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(true,"Reload success."));
        } else {
            return ResponseEntity.badRequest().body(SimpleAjaxResponse.of(false,"Reload fail"));
        }
    }

    @GetMapping("reloadCachedRoleTree")
    public ResponseEntity<?> reloadCachedRoleTree() {
        boolean result = cachedFacade.reloadCachedRoleTree();
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of(true,"Reload success."));
        } else {
            return ResponseEntity.badRequest().body(SimpleAjaxResponse.of(false,"Reload fail"));
        }
    }
}
