package com.hkt.btu.sd.controller;

import com.hkt.btu.sd.facade.SdRequestCreateFacade;
import com.hkt.btu.sd.facade.data.RequestCreateSearchResultsData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@RequestMapping("fault")
@Controller
public class FaultController {

    @Resource(name = "requestCreateFacade")
    SdRequestCreateFacade requestCreateFacade;

    @GetMapping("create")
    public String faultCreate() {
        return "fault/faultCreate";
    }

    @PostMapping("search")
    public ResponseEntity<?> search(String searchKey,String searchValue) {
        RequestCreateSearchResultsData resultsData = requestCreateFacade.searchProductList(searchKey, searchValue);
        if (!StringUtils.isEmpty(resultsData.getErrorMsg())) {
            return ResponseEntity.badRequest().body(resultsData.getErrorMsg());
        } else {
            return ResponseEntity.ok(resultsData.getList());
        }
    }

}
