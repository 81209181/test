package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.controller.response.SimpleAjaxResponse;
import com.hkt.btu.sd.facade.SdServiceTypeFacade;
import com.hkt.btu.sd.facade.data.SdServiceTypeData;
import com.hkt.btu.sd.facade.data.SdServiceTypeOfferMappingData;
import com.hkt.btu.sd.facade.data.UpdateServiceTypeOfferMappingData;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/system/service-type-mapping")
public class ServiceTypeOfferMappingController {

    @Resource
    SdServiceTypeFacade serviceTypeFacade;

    @GetMapping("")
    public String redirectToServiceTypeMapping() {
        return "system/serviceTypeMapping/serviceTypeMapping";
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

    @GetMapping("/service-type-mapping-list")
    public ResponseEntity<?> getServiceTypeMappingList() {
        List<SdServiceTypeOfferMappingData> mappingDataList = serviceTypeFacade.getServiceTypeMappingList();
        return ResponseEntity.ok(mappingDataList);
    }

    @PostMapping("/create-offer-name")
    public ResponseEntity<?> createOfferName(String serviceTypeCode, String offerName) {
        boolean result = serviceTypeFacade.createServiceTypeOfferMapping(serviceTypeCode, offerName);
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.badRequest().body("create failed.");
        }
    }

    @PostMapping("/update-service-type-mapping")
    public ResponseEntity<?> updateServiceTypeOfferMapping(@Validated UpdateServiceTypeOfferMappingData data,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("service type code/offer name is empty or this mapping already existed.");
        }
        boolean result = serviceTypeFacade.updateServiceTypeOfferMapping(data);
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.badRequest().body("update failed.");
        }
    }

    @PostMapping("/delete-service-type-mapping")
    public ResponseEntity<?> deleteServiceTypeOfferMapping(String serviceTypeCode, String offerName) {
        boolean result = serviceTypeFacade.deleteServiceTypeOfferMapping(serviceTypeCode, offerName);
        if (result) {
            return ResponseEntity.ok(SimpleAjaxResponse.of());
        } else {
            return ResponseEntity.badRequest().body("delete failed.");
        }
    }
}
